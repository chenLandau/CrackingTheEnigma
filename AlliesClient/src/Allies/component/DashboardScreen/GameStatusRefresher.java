package Allies.component.DashboardScreen;

import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.CHECK_IF_ALL_USERS_IS_READY;
import static utils.Constants.GSON_INSTANCE;

public class GameStatusRefresher extends TimerTask {
    private final Consumer<Boolean> allUsersIsReadyConsumer;

    public GameStatusRefresher(Consumer<Boolean> allUsersIsReadyConsumer) {
        this.allUsersIsReadyConsumer = allUsersIsReadyConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(CHECK_IF_ALL_USERS_IS_READY)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonAlliesTeamsList = response.body().string();
                //  httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUsersNames);
                Boolean allUsersIsReady = GSON_INSTANCE.fromJson(jsonAlliesTeamsList, Boolean.class);
                allUsersIsReadyConsumer.accept(allUsersIsReady);
            }
        });
    }
}
