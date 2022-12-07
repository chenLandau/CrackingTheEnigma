package Agent.AgentUI.component.ContestScreen;

import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Agent.AgentUI.main.utils.Constants.IS_CONTEST_OVER_CONFIRMED;
import static utils.Constants.GSON_INSTANCE;

public class isContestOverConfirmRefresher extends TimerTask {
    private final Consumer<Boolean> contestOverConfirmConsumer;

    public isContestOverConfirmRefresher(Consumer<Boolean> contestOverConfirmConsumer) {
        this.contestOverConfirmConsumer = contestOverConfirmConsumer;
    }

    @Override
    public void run()  {
        String finalUrl = HttpUrl
                .parse(IS_CONTEST_OVER_CONFIRMED)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Agent: Is contest over confirmed refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonIsContestOverConfirmed = response.body().string();
                response.body().close();
                if (response.code() == 200) {
                    boolean isContestOverConfirmed = GSON_INSTANCE.fromJson(jsonIsContestOverConfirmed, boolean.class);
                    contestOverConfirmConsumer.accept(isContestOverConfirmed);
                } else {
                    System.out.println("Something went wrong!!!");
                }
            }
        });
    }
}