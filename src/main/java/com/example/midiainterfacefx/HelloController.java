package com.example.midiainterfacefx;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    private MediaPlayer mediaPlayer;
    private List<String> musicas;
    private int indexMusicaAtual;
    private double vol = 30;

    @FXML
    private AnchorPane telaAppMusica;
    @FXML
    private MediaView mediaview;
    @FXML
    private Slider tempoMusica, som;
    @FXML
    private Label tempoAtual, tempoTotal, nomeMusica;
    @FXML
    private ImageView audio;

    public void initialize() {
        som.setValue(vol);
        som.valueProperty().addListener((observable, oldValue, newValue) -> {
            vol = newValue.doubleValue();
            mediaPlayer.setVolume(vol / 100);
        });

        carregarMusicas();
        formatarNomeMusica();
        movimentar();
        definirTempo();
        ajustarVolume();
    }

    private void carregarMusicas(){
        musicas = new ArrayList<>();
        // Use caminhos relativos dentro do seu diretório de recursos
        musicas.add(getClass().getResource("/Musica/Death Grips - Get Got.mp3").toString());
        musicas.add(getClass().getResource("/Musica/Death Grips - I've Seen Footage.mp3").toString());
        musicas.add(getClass().getResource("/Musica/Tuff Data - Vans in Japan.mp3").toString());

        indexMusicaAtual = 0;
        String musicaAtual = musicas.get(indexMusicaAtual);

        // Agora você pode carregar o arquivo de música
        Media media = new Media(musicaAtual);
        mediaPlayer = new MediaPlayer(media);
        mediaview.setMediaPlayer(mediaPlayer);
        mediaPlayer.setVolume(vol / 100);
    }

    private void ajustarVolume() {
        som.valueProperty().addListener((observable, oldValue, newValue) -> {
            vol = newValue.doubleValue() / 100; // Atualiza o volume global
            if (vol == 0) {
                mediaPlayer.setMute(true); // Ativa o mudo se o volume for 0
                Image imagem = new Image(getClass().getResource("/com/example/midiainterfacefx/imagens/resources/audioOff.png").toExternalForm());
                audio.setImage(imagem);
            } else {
                mediaPlayer.setMute(false); // Desativa o mudo
                mediaPlayer.setVolume(vol); // Atualiza o volume no player
                Image imagem = new Image(getClass().getResource("/com/example/midiainterfacefx/imagens/resources/audioOn.png").toExternalForm());
                audio.setImage(imagem);
            }
        });
    }


    private void verificarVol(){
        if(vol == 0){
            mediaPlayer.setMute(true);
            mediaPlayer.setVolume(0);
        }
    }

    private void setIndexMusicaAtual() {
        String musicaAtual = musicas.get(indexMusicaAtual);

        Media media = new Media(musicaAtual);
        mediaPlayer = new MediaPlayer(media);
        mediaview.setMediaPlayer(mediaPlayer);

        // Atualiza o volume e estado de mudo
        mediaPlayer.setVolume(vol);
        mediaPlayer.setMute(vol == 0);

        mediaPlayer.play();
        formatarNomeMusica();
        movimentar();
        definirTempo();
    }


    private void definirTempo(){
        mediaPlayer.setOnReady(() -> {
            // Quando a música estiver pronta, definir o tempo total
            Duration tempoAtualMusica = mediaPlayer.getMedia().getDuration();
            int atualSegundos = (int) tempoAtualMusica.toSeconds();
            int minutos = atualSegundos / 60;
            int segundos = atualSegundos % 60;
            String tempoFormatado = String.format("%02d:%02d", minutos, segundos);
            tempoTotal.setText(tempoFormatado);

            // Atualizar o valor máximo do slider com a duração total da música
            tempoMusica.setMax(tempoAtualMusica.toSeconds());
        });

        // Listener para o slider de tempo da música
        tempoMusica.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (tempoMusica.isValueChanging()) {
                // Quando o valor do slider mudar, ajustar o tempo da música
                double posicao = newValue.doubleValue();
                alterarTempoMusica(posicao);
            }
        });

        // Atualiza o tempo atual da música enquanto ela toca
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Duration tempoTotalAtual = mediaPlayer.getCurrentTime();
            int totalSegundos = (int) tempoTotalAtual.toSeconds();
            int minutos = totalSegundos / 60;
            int segundos = totalSegundos % 60;
            String tempoFormatado = String.format("%02d:%02d", minutos, segundos);
            tempoAtual.setText(tempoFormatado);

            // Atualizar o valor do slider com o tempo atual
            tempoMusica.setValue(tempoTotalAtual.toSeconds());
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            proximaMusica();
        });
    }

    private void alterarTempoMusica(double posicao) {
        // Alterar o tempo da música conforme a posição do slider
        Duration novaDuracao = Duration.seconds(posicao);
        mediaPlayer.seek(novaDuracao);
    }


    private void formatarNomeMusica() {
        if (musicas != null && !musicas.isEmpty() && indexMusicaAtual >= 0 && indexMusicaAtual < musicas.size()) {
            // Obtem o caminho completo da música
            String caminhoCompleto = musicas.get(indexMusicaAtual);

            // Localiza a posição da última ocorrência de "/Musica/" e extrai o nome
            String nomeMusicaFormatado = caminhoCompleto.substring(caminhoCompleto.lastIndexOf("/Musica/") + 8);

            // Atualiza o texto do Label
            nomeMusicaFormatado = URLDecoder.decode(nomeMusicaFormatado, StandardCharsets.UTF_8);
            nomeMusica.setText(nomeMusicaFormatado);
        }
    }

    private void movimentar(){
        TranslateTransition transition = new TranslateTransition(Duration.seconds(15), nomeMusica);

        transition.setFromX(300);
        transition.setToX(-200);

        transition.setCycleCount(TranslateTransition.INDEFINITE);

        transition.play();
    }

    public void minimizar(){
        ((Stage)telaAppMusica.getScene().getWindow()).toBack();
    }

    public void fechar(){
        ((Stage)telaAppMusica.getScene().getWindow()).close();
    }

    public void musicaAnterior(){
        mediaPlayer.stop();
        indexMusicaAtual--;

        if(indexMusicaAtual < 0){
            indexMusicaAtual = musicas.size() - 1;
        }

        setIndexMusicaAtual();
    }

    public void proximaMusica(){
        mediaPlayer.stop();
        indexMusicaAtual++;

        if(indexMusicaAtual > musicas.size() - 1){
            indexMusicaAtual = 0;
        }

        setIndexMusicaAtual();
    }

    public void playMusica(){
        mediaPlayer.play();
        verificarVol();
    }

    public void pausarMusica(){
        mediaPlayer.pause();
    }

    public void stopMusica(){
        mediaPlayer.stop();
    }

    public void mutarMusica() {
        if (mediaPlayer.isMute()) {
            // Desmuta e restaura o volume original
            mediaPlayer.setMute(false);
            mediaPlayer.setVolume(vol);
            Image imagem = new Image(getClass().getResource("/com/example/midiainterfacefx/imagens/resources/audioOn.png").toExternalForm());
            audio.setImage(imagem);
        } else {
            // Muta, mas mantém o valor do slider
            mediaPlayer.setMute(true);
            Image imagem = new Image(getClass().getResource("/com/example/midiainterfacefx/imagens/resources/audioOff.png").toExternalForm());
            audio.setImage(imagem);
        }
    }

}