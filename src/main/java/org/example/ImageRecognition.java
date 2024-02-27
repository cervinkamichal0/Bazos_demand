
package org.example;


import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;

public class ImageRecognition {

    /////////////////////////////////////////////////////////////////////////////////
    // In this section, we set the user authentication, app ID, workflow ID, and
    // image URL. Change these strings to run your own example.
    ////////////////////////////////////////////////////////////////////////////////

    static final String USER_ID = "x8jcxmq2nz63";
    //Your PAT (Personal Access Token) can be found in the portal under Authentication
    static final String PAT = "1a5e2269a4804cbb91f194e77247a945";
    static final String APP_ID = "spin";
    // Change these to make your own predictions
    static final String WORKFLOW_ID = "image_recognition";


    ///////////////////////////////////////////////////////////////////////////////////
    // YOU DO NOT NEED TO CHANGE ANYTHING BELOW THIS LINE TO RUN THIS EXAMPLE
    ///////////////////////////////////////////////////////////////////////////////////

    public static boolean isCar(String imageURL) {

        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials(PAT));

        PostWorkflowResultsResponse postWorkflowResultsResponse = stub.postWorkflowResults(
                PostWorkflowResultsRequest.newBuilder()
                        .setUserAppId(UserAppIDSet.newBuilder().setUserId(USER_ID).setAppId(APP_ID))
                        .setWorkflowId(WORKFLOW_ID)
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setUrl(imageURL)
                                        )
                                )
                        )
                        .build()
        );

        if (postWorkflowResultsResponse.getStatus().getCode() != StatusCode.SUCCESS) {

            return true;
        }

        // We'll get one WorkflowResult for each input we used above. Because of one input, we have here
        // one WorkflowResult.
        WorkflowResult results = postWorkflowResultsResponse.getResults(0);

        // Each model we have in the workflow will produce one output.
        for (Output output: results.getOutputsList()) {
            Model model = output.getModel();

           String firsConceptName = output.getData().getConceptsList().get(0).getName();


            if (!firsConceptName.equals("car") && !firsConceptName.equals("vehicle")){
                System.out.println("Filtr: " + firsConceptName);
                return false;
            }


        }
        return true;
    }

}
