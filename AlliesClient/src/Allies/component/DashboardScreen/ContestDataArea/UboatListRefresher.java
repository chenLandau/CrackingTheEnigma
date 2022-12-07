package Allies.component.DashboardScreen.ContestDataArea;

import Allies.main.util.Constants;
import DataTransferObject.UboatDTO;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;


public class UboatListRefresher extends TimerTask {
    private final Consumer<List<UboatDTO>> uboatListConsumer;
    public UboatListRefresher(Consumer<List<UboatDTO>> uboatListConsumer) {
        this.uboatListConsumer = uboatListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.UBOAT_LIST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Ally: Get uboat list refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                UboatDTO[] uboatNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, UboatDTO[].class);
                uboatListConsumer.accept(Arrays.asList(uboatNames));
            }
        });
    }
}
