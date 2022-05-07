package hedera22.hackathon.smartcontractfx;
//https://stackoverflow.com/questions/52457813/javafx-11-add-a-graphic-to-a-titledpane-on-the-right-side

import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import java.beans.EventHandler;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
//mport static hedera22.hackathon.smartcontractfx.SystemInfo.jsonOutputFromSolidity;

/**
 * JavaFX App//
 */
public class App extends Application {

    Task myWorker;
    Task myWorker2;

    BorderPane borderPane = new BorderPane();

    final TabPane tabPane = new TabPane();
    final Tab deploySmartContract = new Tab("Smart Contract Deployment");

    final Tab executeSmartContract = new Tab("Smart Contract Execution");
    final Tab deleteSmartContract = new Tab("Delete Smart Contract");

    final TextArea textAreaContracttextArea = new TextArea("");
    TitledPane tptextAreaContracttextAre;
    final TextField tfHederaAccount = new TextField();
    final TextField tfPrivateKey = new TextField();

    final Button buttonDeploy = new Button("Deploy Smart Contract to the Hedera Network");
    final Button buttonExecuteSmartContract = new Button("Execute Smart Contract");
    final HBox hb_buttonDeployAndTaskProgress = new HBox(10);
    final HBox hb_buttonExecuteAndTaskProgress = new HBox(10);
    //StringProperty contractIdProperty = new SimpleStringProperty("");

    final TextArea textAreaResult = new TextArea("");
    TitledPane tptextAreaResult;

    final TextField tfContractid = new TextField();
    final TextField tfFunctionName = new TextField();
    final TextField tfFunctionParameters = new TextField();

    final TextArea textAreaExecResult = new TextArea("");
    TitledPane tptextAreaExecResult;

    final ChoiceBox networkChoiceBox = new ChoiceBox(FXCollections.observableArrayList("TESTNET", "MAINNET"));
    StringProperty netWorkProperty = new SimpleStringProperty("");
    StringProperty availableContractFunctionsproperty = new SimpleStringProperty("");
    Font fnt14 = javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 14);

    StringProperty constructorParamsType = new SimpleStringProperty("");
    final TextField tfConstructorParamValue = new TextField();
    final Label lbConstructorParamsType = new Label();

    // Button dummy = new Button();
    @Override
    @SuppressWarnings("empty-statement")
    public void start(Stage stage) {

        constructorParamsType.setValue(SystemInfo.getSolidityConstructorFromJson(SolidityContractJSONCode.jsonOutputFromSolidity));
        tfConstructorParamValue.setText("Hello Hedera");

        textAreaContracttextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String consParm = SystemInfo.getSolidityConstructorFromJson(newValue);
                    System.out.println("consParm = " + consParm);
                    constructorParamsType.setValue(consParm);

                }
            });

        });
        lbConstructorParamsType.textProperty().bind(constructorParamsType);
        //tabPane.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " );
        stage.setTitle("HEDERA SMART CONTRACTS");

        tabPane.getStyleClass().add("floating");
        // textAreaResult.setFont(fnt14);
        textAreaContracttextArea.setFont(fnt14);

        textAreaContracttextArea.setText(SolidityContractJSONCode.jsonOutputFromSolidity);
        tptextAreaContracttextAre = new TitledPane("Enter contract JSON file here or use the following default:", textAreaContracttextArea);

        tptextAreaContracttextAre.setCollapsible(false);

        tptextAreaResult = new TitledPane("Deployment notification", textAreaResult);

        tptextAreaResult.setCollapsible(false);
        textAreaResult.setEditable(false);

        tptextAreaExecResult = new TitledPane("Contract execution notification", textAreaExecResult);

        tptextAreaExecResult.setCollapsible(false);
        textAreaExecResult.setEditable(false);
        //textAreaExecResult.setPrefHeight(Integer.MAX_VALUE);

        //final ProgressBar progressBar = new ProgressBar(0);
        final ProgressIndicator progressIndicator = new ProgressIndicator(0);

        // final ProgressBar progressBar2 = new ProgressBar(0);
        final ProgressIndicator progressIndicator2 = new ProgressIndicator(0);
        // buttonDeploy.setStyle("-fx-font-size: 2em; ");

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        hb_buttonDeployAndTaskProgress.getChildren().add(buttonDeploy);
        hb_buttonExecuteAndTaskProgress.getChildren().add(buttonExecuteSmartContract);
        // BorderPane borderPane = new BorderPane();
        ToolBar toolbar = new ToolBar();
        HBox statusbar = new HBox();
        statusbar.getChildren().add(new Label(""));
        VBox center = new VBox();
        //center.setAlignment(Pos.TOP_LEFT);
        deploySmartContract.setContent(getDeploySmartContractContent());
        executeSmartContract.setContent(getExecuteSmartContractContent());
        center.getChildren().addAll(getAccountInfo(), getContractTapPane());
        // Node appContent = new Label("Hello, JavaFX ");
        borderPane.setTop(toolbar);
        borderPane.setCenter(center);
        borderPane.setBottom(statusbar);

       
        Scene scene = new Scene(borderPane, screenBounds.getWidth() * .8, screenBounds.getHeight() * .9);
       
        scene.getStylesheets().add("/style.css");

        stage.setScene(scene);
        tabPane.prefWidthProperty().bind(scene.widthProperty().subtract(3.0));

        // labelPleaseEnterSmartContract.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 16));
        buttonDeploy.setOnAction((ActionEvent event) -> {

            //deployContract();
            try {
                textAreaResult.setText("");

                myWorker = createTask();

                // progressBar.progressProperty().bind(myWorker.progressProperty());
                //  progressIndicator.progressProperty().unbind();
                progressIndicator.progressProperty().bind(myWorker.progressProperty());

                myWorker.stateProperty().addListener(new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                        //HBox h = new HBox();

                        // hb_buttonDeployAndTaskProgress.getChildren().add(progressIndicator);
                        hb_buttonDeployAndTaskProgress.setAlignment(Pos.CENTER_LEFT);
                        if (newValue == Worker.State.SUCCEEDED) {
                            hb_buttonDeployAndTaskProgress.getChildren().remove(progressIndicator);
                        } else if (newValue == Worker.State.RUNNING) {
                            // HBox.setHgrow(h, Priority.NEVER);
                            hb_buttonDeployAndTaskProgress.getChildren().add(progressIndicator);
                        }
                    }
                });

                new Thread(myWorker).start();

            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        buttonExecuteSmartContract.setOnAction((ActionEvent event) -> {

            //deployContract();
            try {
                textAreaExecResult.setText("");
                myWorker2 = createTask2();

                //   progressBar2.progressProperty().bind(myWorker2.progressProperty());
                //  progressIndicator.progressProperty().unbind();
                progressIndicator2.progressProperty().bind(myWorker2.progressProperty());

                myWorker2.stateProperty().addListener(new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                        //HBox h = new HBox();

                        // hb_buttonDeployAndTaskProgress.getChildren().add(progressIndicator);
                        hb_buttonExecuteAndTaskProgress.setAlignment(Pos.CENTER_LEFT);
                        if (newValue == Worker.State.SUCCEEDED) {
                            hb_buttonExecuteAndTaskProgress.getChildren().remove(progressIndicator2);
                        } else if (newValue == Worker.State.RUNNING) {
                            // HBox.setHgrow(h, Priority.NEVER);
                            hb_buttonExecuteAndTaskProgress.getChildren().add(progressIndicator2);
                        }
                    }
                });

                new Thread(myWorker2).start();

            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        networkChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_toggle, Number new_toggle) {
                if (new_toggle != null) {
                    if (new_toggle.intValue() == 0) {

                        netWorkProperty.setValue("TESTNET");
                        // System.out.println("Testnet selected");
                        tfHederaAccount.setText("0.0.16447089");

                        tfPrivateKey.setText("302e020100300506032b6570042204200ea60b801334a524e373ef964ad0663ca9f3e8511a9d4a29afdfb990b1f0cbbc");
                    } else if (new_toggle.intValue() == 1) {

                        netWorkProperty.setValue("MAINNET");
                        // System.out.println("Mainnet selected");
                        tfHederaAccount.setText("");
                        tfPrivateKey.setText("");
                        tfContractid.setText("");
                        tfFunctionName.setText("");

                    } else if (new_toggle.intValue() == 2) {

                    }

                }
            }
        });
        networkChoiceBox.getSelectionModel().selectFirst();
        stage.show();

        //SystemInfo.getSolidityFunctionsFromJson(SolidityContractJSONCode.jsonOutputFromSolidity);
        //label.textProperty().bind(Bindings.format("Some text: %s", otherProperty()))
    }

    public static void main(String[] args) {
        launch();
    }

    public GridPane getAccountInfo() {
//        HBox h = new HBox();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Hedera Account Info");
        // scenetitle.setId("welcome-text");
        scenetitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label hNetwork = new Label("Hedera network:");
        hNetwork.setFont(fnt14);
        grid.add(hNetwork, 0, 1);

        //final TextField pwBox = new TextField();
        grid.add(networkChoiceBox, 1, 1);

        Label lbHederaAccount = new Label("Hedera Account:");
        lbHederaAccount.setId("hederaAccount");
        lbHederaAccount.setFont(fnt14);
        grid.add(lbHederaAccount, 0, 2);

        //final TextField tfHederaAccount = new TextField();
        tfHederaAccount.setText("0.0.16447089");
        grid.add(tfHederaAccount, 1, 2);

        Label privateKeyLabel = new Label("Private Key:");
        privateKeyLabel.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 14));
        grid.add(privateKeyLabel, 0, 3);

        //final TextField tfPrivateKey = new TextField();
        tfPrivateKey.setText("302e020100300506032b6570042204200ea60b801334a524e373ef964ad0663ca9f3e8511a9d4a29afdfb990b1f0cbbc");
        grid.add(tfPrivateKey, 1, 3);

        return grid;

    }

    public GridPane getConstructorInfo() {
//        HBox h = new HBox();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text constructorTitle = new Text("Solidity Constructor Info");
        // scenetitle.setId("welcome-text");
        constructorTitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(constructorTitle, 0, 0, 2, 1);

        Label paramsType = new Label("Constructor parameters type:");
        paramsType.setId("paramType");
        paramsType.setFont(fnt14);
        grid.add(paramsType, 0, 1);

        //final TextField pwBox = new TextField();
        grid.add(lbConstructorParamsType, 1, 1);

        Label constructorParamValue = new Label("Constructor parameter value:");
        constructorParamValue.setId("constructorParamValue");
        constructorParamValue.setFont(fnt14);
        grid.add(constructorParamValue, 0, 2);

        //final TextField tfConstructorParamValue = new TextField();
        tfHederaAccount.setText("0.0.16447089");
        grid.add(tfConstructorParamValue, 1, 2);

        return grid;

    }

    public HBox getContractTapPane() {
        HBox hbox = new HBox();
        // hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setPadding(new Insets(2, 2, 2, 2));
        hbox.setSpacing(10);
        // hbox.setStyle("-fx-background-color: #336699;");
        //  hbox.setStyle("-fx-background-color: lightgray;");

        // Button buttonCurrent = new Button("Current");
        // buttonCurrent.setPrefSize(100, 20);
        // Button buttonProjected = new Button("Projected");
        // buttonProjected.setPrefSize(100, 20);
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //  tabPane.setRotateGraphic(true);
        tabPane.setTabMinHeight(30);

        tabPane.getTabs().addAll(deploySmartContract, executeSmartContract); //, deleteSmartContract);
        hbox.getChildren().addAll(
                tabPane
        );

        return hbox;
    }

    public VBox getDeploySmartContractContent() {
        VBox vb = new VBox(15);
        vb.setPadding(new Insets(5, 5, 5, 5));
        vb.setAlignment(Pos.CENTER_LEFT);

        vb.getChildren().addAll(tptextAreaContracttextAre, getConstructorInfo(), hb_buttonDeployAndTaskProgress, tptextAreaResult);

        return vb;

    }

    public VBox getExecuteSmartContractContent() {

        VBox vb = new VBox(8);
        vb.setPadding(new Insets(5, 5, 5, 5));
        vb.setAlignment(Pos.TOP_LEFT);
        //VBox.setVgrow(tptextAreaExecResult, Priority.ALWAYS);
        var spacer = new Region();
        spacer.setPrefHeight(20);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        vb.getChildren().addAll(getContractInfoGrid(), hb_buttonExecuteAndTaskProgress, spacer, tptextAreaExecResult);

        return vb;

    }

    /*
    public void deployContract() {
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
        }

    }
     */
    public void deployContract() {
        //Platform.runLater(() -> {
        StringBuilder sb = new StringBuilder();
        try {
            long start = System.currentTimeMillis();
            FileId fileId = SystemInfo.storeSmartContractByteCodeOnHedera(tfHederaAccount.getText(), tfPrivateKey.getText(), textAreaContracttextArea.getText(), netWorkProperty.getValue());

            System.out.println("--------------------------------------");
            ContractId contractId = SystemInfo.deployHederaSmartContract(tfHederaAccount.getText(), tfPrivateKey.getText(), fileId, netWorkProperty.getValue(), tfConstructorParamValue.getText());
            // contractIdProperty.setValue(contractId.toString());
            tfContractid.setText(contractId.toString());
            tfFunctionName.setText("get_message");
            Set<String> availableFunction = SystemInfo.getSolidityFunctionsFromJson(textAreaContracttextArea.getText());
            System.out.println(availableFunction);
            availableContractFunctionsproperty.setValue(availableFunction.toString());

            long end = System.currentTimeMillis();
            //finding the time difference and converting it into seconds
            float sec = (end - start) / 1000F;
            System.out.println(sec + " seconds");

            sb.append("Smart contract deployed to the Hedera network successfully" + "\n");
            sb.append("Deployment time: " + sec + " seconds" + "\n");
            sb.append("Smart contract bytecode file ID : " + fileId.toString() + "\n");
            sb.append("Smart Contract Id: " + contractId.toString());
            sb.append("\n");
            sb.append("\n");
            sb.append(".\n");
            textAreaResult.setText(sb.toString());

        } catch (TimeoutException ex) {
            sb.append(ex.getMessage());
            textAreaResult.setText(sb.toString());
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrecheckStatusException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
            textAreaResult.setText(sb.toString());
        } catch (ReceiptStatusException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
            textAreaResult.setText(sb.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
            textAreaResult.setText(sb.toString());
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
            textAreaResult.setText(sb.toString());

        } finally {
            // textAreaResult.setText(sb.toString());

        }

    }

    public void executeContract() {
        //Platform.runLater(() -> {
        StringBuilder sb = new StringBuilder();
        try {

            if (!tfContractid.getText().startsWith("0.0")) {
                throw new ContractIdFormatException(tfContractid.getText());
            }
            ContractId contractId = ContractId.fromString(tfContractid.getText());
            String message = "";
            if (tfFunctionName.getText().trim().startsWith("get")) {
                message = SystemInfo.callContractNoParamQuery(tfHederaAccount.getText(), tfPrivateKey.getText(), contractId, netWorkProperty.getValue(), tfFunctionName.getText());
            } else {
                message = SystemInfo.callContractOneParamTransaction(tfHederaAccount.getText(), tfPrivateKey.getText(), contractId, netWorkProperty.getValue(), tfFunctionName.getText(), tfFunctionParameters.getText());
            }

            sb.append(message + "\n");
            // sb.append("function return value is : " +  message);
            textAreaExecResult.setText(sb.toString());
            System.out.println("--------------------------------------");

        } catch (ContractIdFormatException ex) {
            sb.append(ex);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);

        } catch (TimeoutException ex) {
            sb.append(ex);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrecheckStatusException ex) {
            sb.append(ex);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
        } finally {
            textAreaExecResult.setText(sb.toString());
        }
        // }
        //);
    }

    public Task createTask() throws Exception {
        return new Task() {
            @Override
            public Void call() throws Exception {
                deployContract();

                return null;
            }

        };

    }

    public Task createTask2() throws Exception {
        return new Task() {
            @Override
            public Void call() throws Exception {
                /*
                for (int i = 0; i < 100000; i++) {
                    System.out.println(i);
                }
                 */
                executeContract();
                return null;
            }

        };

    }

    public GridPane getContractInfoGrid() {
//        HBox h = new HBox();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Font labelFont = javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 14);
        Label lbContractId = new Label("Contract ID:");
        lbContractId.setId("contractId");
        lbContractId.setFont(labelFont);
        grid.add(lbContractId, 0, 1);
        grid.add(tfContractid, 1, 1);

        Label fn = new Label("Function name:");
        fn.setId("fn");
        fn.setFont(labelFont);
        grid.add(fn, 0, 2);
        grid.add(tfFunctionName, 1, 2);

        Label s = new Label("Available contract external functions: ");
        s.setFont(labelFont);
        grid.add(s, 2, 2);

        Text t = new Text();
        t.textProperty().bind(availableContractFunctionsproperty);
        grid.add(t, 3, 2);

        Label fnParameters = new Label("Function parameters:");
        fnParameters.setFont(labelFont);
        fnParameters.setId("fnParameters");
        grid.add(fnParameters, 0, 3);
        grid.add(tfFunctionParameters, 1, 3);

        Label l = new Label("Parameters are | separated. Sample: a|b");
        l.setFont(labelFont);
        grid.add(l, 2, 3);

        return grid;

    }

}
