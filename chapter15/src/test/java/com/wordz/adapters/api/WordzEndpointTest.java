package com.wordz.adapters.api;

import com.google.gson.Gson;
import com.vtence.molecule.http.HttpStatus;
import com.wordz.domain.GuessResult;
import com.wordz.domain.Player;
import com.wordz.domain.Score;
import com.wordz.domain.Wordz;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.vtence.molecule.testing.http.HttpResponseAssert.assertThat;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WordzEndpointTest {
    private WordzEndpoint endpoint;

    private Wordz mockWordz;

    private final Player player = new Player("alan2112");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeAll
    void setUp() {
        mockWordz = mock(Wordz.class);
        endpoint = new WordzEndpoint(mockWordz, "localhost", 8080);
    }

    @Test
    void startsGame() throws Exception {
        when(mockWordz.newGame(eq(player))).thenReturn(true);

        var req = requestBuilder("start")
                .POST(asJsonBody(player))
                .build();

        var res
                = httpClient.send(req,
                HttpResponse.BodyHandlers.discarding());

        assertThat(res).hasStatusCode(HttpStatus.NO_CONTENT.code);
    }

    @Test
    void rejectsRestart() throws Exception {
        when(mockWordz.newGame(eq(player))).thenReturn(false);

        var req = requestBuilder("start")
                .POST(asJsonBody(player))
                .build();

        var res
                = httpClient.send(req,
                HttpResponse.BodyHandlers.discarding());

        assertThat(res).hasStatusCode(HttpStatus.CONFLICT.code);
    }

    @Test
    void rejectsMalformedRequest() throws Exception {
        var req = requestBuilder("start")
                .POST(ofString("malformed"))
                .build();

        var res
                = httpClient.send(req,
                HttpResponse.BodyHandlers.discarding());

        assertThat(res).hasStatusCode(HttpStatus.BAD_REQUEST.code);
    }

    @Test
    void partiallyCorrectGuess() throws Exception {
        // Score: part correct, correct, incorrect, incorrect, incorrect
        var score = new Score("-U--G");
        score.assess("GUESS");

        var result = new GuessResult(score, false, false);
        when(mockWordz.assess(eq(player), eq("GUESS")))
                .thenReturn(result);

        var guessRequest = new GuessRequest(player, "GUESS");
        var body = new Gson().toJson(guessRequest);
        var req = requestBuilder("guess")
                .POST(ofString(body))
                .build();

        var res
                = httpClient.send(req,
                HttpResponse.BodyHandlers.ofString());

        var response
                = new Gson().fromJson(res.body(), GuessHttpResponse.class);

        // Key to letters in scores():
        // C correct, P part correct, X incorrect
        Assertions.assertThat(response.scores()).isEqualTo("PCXXX");
        Assertions.assertThat(response.isGameOver()).isFalse();
    }

    @Test
    void reportsGameOver() throws Exception {
        var result = new GuessResult(new Score("-----"), true, false);
        when(mockWordz.assess(eq(player), eq("GUESS")))
                .thenReturn(result);

        var guessRequest = new GuessRequest(player, "GUESS");
        var body = new Gson().toJson(guessRequest);
        var req = requestBuilder("guess")
                .POST(ofString(body))
                .build();

        var res
                = httpClient.send(req,
                HttpResponse.BodyHandlers.ofString());

        var response
                = new Gson().fromJson(res.body(), GuessHttpResponse.class);

        Assertions.assertThat(response.isGameOver()).isTrue();
    }

    @Test
    void reportsError() throws Exception {
        var result = new GuessResult(new Score("-----"),
                              false, true);
        when(mockWordz.assess(eq(player), eq("GUESS")))
                .thenReturn(result);

        var guessRequest = new GuessRequest(player, "GUESS");
        var body = new Gson().toJson(guessRequest);
        var req = requestBuilder("guess")
                .POST(ofString(body))
                .build();

        var res
                = httpClient.send(req,
                HttpResponse.BodyHandlers.ofString());

        assertThat(res).hasStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.code);
    }

    private HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + path));
    }

    private HttpRequest.BodyPublisher asJsonBody(Object source) {
        return ofString(new Gson().toJson(source));
    }
}
