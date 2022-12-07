package Uboat.component.ContestScreen;

import DataTransferObject.AllyTeamDTO;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Uboat.main.utils.Constants.GET_ALLIES_TEAMS_LIST;
import static utils.Constants.*;

public class GameStatusRefresher extends TimerTask {
    private BooleanProperty shouldUpdate;
    public GameStatusRefresher(BooleanProperty shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(CHECK_IF_ALL_USERS_IS_READY)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Uboat: Check if all users is ready refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               String jsonAlliesTeamsList = response.body().string();
               Boolean allUsersIsReady = GSON_INSTANCE.fromJson(jsonAlliesTeamsList, Boolean.class);
                if(allUsersIsReady){
                    shouldUpdate.set(false);
                }
            }
        });
    }
}
