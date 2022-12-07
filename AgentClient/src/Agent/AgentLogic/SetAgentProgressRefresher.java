package Agent.AgentLogic;

import DataTransferObject.AgentProgressDTO;
import com.sun.istack.internal.NotNull;
import okhttp3.*;
import utils.http.HttpClientUtil;
import java.io.IOException;
import java.util.TimerTask;

import static Agent.AgentUI.main.utils.Constants.*;
import static utils.Constants.GSON_INSTANCE;

public class SetAgentProgressRefresher extends TimerTask {
    private AgentMissionCounter agentMissionCounter;
    private String userName;

    public SetAgentProgressRefresher(AgentMissionCounter agentMissionCounter, String userName) {
       this.agentMissionCounter = agentMissionCounter;
       this.userName = userName;
    }

    @Override
    public void run() {
        AgentProgressDTO agentProgressDTO = new AgentProgressDTO(userName, agentMissionCounter.getMissionsOnQueue(),
                agentMissionCounter.getTotalAmountOfMissionTaken(), agentMissionCounter.getMissionsCompleted(),
                agentMissionCounter.getTotalCandidatesFound());

        String body = GSON_INSTANCE.toJson(agentProgressDTO);

        String finalUrl = HttpUrl
                .parse(SET_AGENT_PROGRESS)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder().url(finalUrl)
                .post(RequestBody.create(body.getBytes()))
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Agent: Set agent progress refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonMissionList = response.body().string();
            }
        });
    }
}
