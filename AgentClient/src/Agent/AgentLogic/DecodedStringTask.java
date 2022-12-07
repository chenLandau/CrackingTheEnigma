package Agent.AgentLogic;

import Agent.AgentUI.main.utils.UIAdapter;
import DataTransferObject.CandidateDTO;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import static Agent.AgentUI.main.utils.Constants.ADD_NEW_CANDIDATES;
import static utils.Constants.GSON_INSTANCE;

public class DecodedStringTask extends Task<Boolean> {
    private BlockingQueue<List<CandidateDTO>> candidateDecodedString;
    private UIAdapter uiAdapter;
    private BooleanProperty shouldUpdateCandidates;
    private long totalCandidatesFound;

    public DecodedStringTask(BlockingQueue<List<CandidateDTO>> candidateDecodedString, UIAdapter uiAdapter, BooleanProperty shouldUpdateCandidates) {
        this.candidateDecodedString = candidateDecodedString;
        this.uiAdapter = uiAdapter;
        this.shouldUpdateCandidates = shouldUpdateCandidates;
    }

    @Override
    protected Boolean call() {
        try {
            while (shouldUpdateCandidates.get()) {
                List<CandidateDTO> decodedStringDTOList;

                if (!candidateDecodedString.isEmpty()) {
                    synchronized (candidateDecodedString) {
                        decodedStringDTOList = candidateDecodedString.take();
                    }
                    totalCandidatesFound += decodedStringDTOList.size();

                    for(CandidateDTO candidateDTO: decodedStringDTOList){
                        uiAdapter.addNewDecryptOptionalString(candidateDTO);
                        uiAdapter.setNumberOfCandidateFoundString(totalCandidatesFound);
                    }

                    String json = GSON_INSTANCE.toJson(decodedStringDTOList);
                    RequestBody requestBody = HttpClientUtil.createRequestBody("text/plain", json);

                    String finalUrl = HttpUrl
                            .parse(ADD_NEW_CANDIDATES)
                            .newBuilder()
                            .build()
                            .toString();

                    HttpClientUtil.runRequestWithBody(finalUrl, requestBody, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            System.out.println("Fail on Agent: DecodedStringTask");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.code() != 200) {
                                System.out.println("something went wrong on Agent: DecodedStringTask");
                            }
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }
}