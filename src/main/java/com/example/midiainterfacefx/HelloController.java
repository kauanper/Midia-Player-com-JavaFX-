package com.example.midiainterfacefx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    private MediaPlayer mediaPlayer;
    private List<String> musicas;
    private int indexMusicaAtual;

    @FXML
    private AnchorPane telaAppMusica;
    @FXML
    private MediaView mediaview;
    @FXML
    private Slider tempoMusica, som;
    @FXML
    private Label tempoAtual, tempoTotal, nomeMusica;

    public void initialize() {
        carregarMusicas();
        nomeMusica.setText("Teste de Atualização");
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
    }

    private void setIndexMusicaAtual(){
        String musicaAtual = musicas.get(indexMusicaAtual);

        Media media = new Media(musicaAtual);
        mediaPlayer = new MediaPlayer(media);
        mediaview.setMediaPlayer(mediaPlayer);

        mediaPlayer.play();
        formatarNomeMusica();
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
        formatarNomeMusica();
    }

    public void pausarMusica(){
        mediaPlayer.pause();
    }

    public void stopMusica(){
        mediaPlayer.stop();
    }

    public void mutarMusica(){

    }
}