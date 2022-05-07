package hedera22.hackathon.smartcontractfx;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hedera.hashgraph.sdk.AccountId;
//import com.hedera.hashgraph.sdk.PreCheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.ContractCallQuery;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.proto.ResponseCodeEnum;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
//import io.github.cdimascio.dotenv.Dotenv;​
import java.util.concurrent.TimeoutException;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class SystemInfo {

    //public static String hederaAccount = "0.0.33993979";
    //public static String hederaPrivateKey = "302e020100300506032b6570042204205d155b76ebb890bc2c8b90aa710d534baa69468cc2214d2150e560f8d43911d0";
    //public static String hederaAccount = "0.0.16447089";
    //public static String hederaPrivateKey = "302e020100300506032b6570042204200ea60b801334a524e373ef964ad0663ca9f3e8511a9d4a29afdfb990b1f0cbbc";
    public static Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    public static void main(String args[]) {
        getSolidityFunctionsFromJson(SolidityContractJSONCode.jsonOutputFromSolidity);

    }

    public static String getSolidityConstructorFromJson(String jsonOutputFromSolididy) {
      try {
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject) jsonParser.parse(jsonOutputFromSolididy);
        // System.out.println(jo);

        JsonArray jABI = jo.getAsJsonArray("abi");

        System.out.println(jABI);

        for (JsonElement pa : jABI) {
            JsonObject solABI = pa.getAsJsonObject();
            String type = solABI.get("type").getAsString();
            System.out.println("type " + type);
            if (type.equals("constructor")) {
                JsonArray inputs = solABI.get("inputs").getAsJsonArray();
                System.out.println("inputs " + inputs);
                return inputs.toString();

            }

        }
      }
      catch (Exception e ) {
      return "";
      } 
      
      
      return "";

    }

    public static Set<String> getSolidityFunctionsFromJson(String jsonOutputFromSolidity) {

        //Import the compiled contract from the HelloHedera.json file
        Gson gson = new Gson();
        JsonObject jsonObject;

        // InputStream jsonStream = new FileInputStream("C:\\Users\\shakir.gusaroff\\Documents\\NetBeansProjects\\SmartContractFX\\src\\main\\java\\hedera22\\hackathon\\smartcontractfx\\HelloHedera.json");
        InputStream jsonStream = new ByteArrayInputStream(jsonOutputFromSolidity.getBytes());
        System.out.println("jsonstream = " + jsonStream);
        jsonObject = gson.fromJson(new InputStreamReader(jsonStream, StandardCharsets.UTF_8), JsonObject.class);
        // System.out.println("jsonObject = " + jsonObject);
//Store the "object" field from the HelloHedera.json file as hex-encoded bytecode
        //String object = jsonObject.getAsJsonObject("data").getAsJsonObject("external").get("object").getAsString();
        JsonObject jobject = jsonObject.getAsJsonObject("data").getAsJsonObject("gasEstimates").getAsJsonObject("external");
        Set<String> keys = jobject.keySet();
        // JsonArray jarray = jobject.getAsJsonArray();
        System.out.print(keys);
        return keys;

//Submit the file to the Hedera test network signing with the transaction fee payer key specified with the client
    }

    /**
     *
     */
    /*
    public static String jsonOutputFromSolidity = """
                                  {
                                        "deploy": {
                                          "VM:-": {
                                            "linkReferences": {},
                                            "autoDeployLib": true
                                          },
                                          "main:1": {
                                            "linkReferences": {},
                                            "autoDeployLib": true
                                          },
                                          "ropsten:3": {
                                            "linkReferences": {},
                                            "autoDeployLib": true
                                          },
                                          "rinkeby:4": {
                                            "linkReferences": {},
                                            "autoDeployLib": true
                                          },
                                          "kovan:42": {
                                            "linkReferences": {},
                                            "autoDeployLib": true
                                          },
                                          "görli:5": {
                                            "linkReferences": {},
                                            "autoDeployLib": true
                                          },
                                          "Custom": {
                                            "linkReferences": {},
                                            "autoDeployLib": true
                                          }
                                        },
                                        "data": {
                                          "bytecode": {
                                            "linkReferences": {},
                                            "object": "608060405234801561001057600080fd5b506040516105583803806105588339818101604052602081101561003357600080fd5b810190808051604051939291908464010000000082111561005357600080fd5b8382019150602082018581111561006957600080fd5b825186600182028301116401000000008211171561008657600080fd5b8083526020830192505050908051906020019080838360005b838110156100ba57808201518184015260208101905061009f565b50505050905090810190601f1680156100e75780820380516001836020036101000a031916815260200191505b50604052505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806001908051906020019061014492919061014b565b50506101e8565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061018c57805160ff19168380011785556101ba565b828001600101855582156101ba579182015b828111156101b957825182559160200191906001019061019e565b5b5090506101c791906101cb565b5090565b5b808211156101e45760008160009055506001016101cc565b5090565b610361806101f76000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80632e9826021461003b57806332af2edb146100f6575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610179565b005b6100fe6101ec565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561013e578082015181840152602081019050610123565b50505050905090810190601f16801561016b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146101d1576101e9565b80600190805190602001906101e792919061028e565b505b50565b606060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102845780601f1061025957610100808354040283529160200191610284565b820191906000526020600020905b81548152906001019060200180831161026757829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102cf57805160ff19168380011785556102fd565b828001600101855582156102fd579182015b828111156102fc5782518255916020019190600101906102e1565b5b50905061030a919061030e565b5090565b5b8082111561032757600081600090555060010161030f565b509056fea26469706673582212201644465f5f73dfd73a518b57770f5adb27f025842235980d7a0f4e15b1acb18e64736f6c63430007000033",
                                            "opcodes": "PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x40 MLOAD PUSH2 0x558 CODESIZE SUB DUP1 PUSH2 0x558 DUP4 CODECOPY DUP2 DUP2 ADD PUSH1 0x40 MSTORE PUSH1 0x20 DUP2 LT ISZERO PUSH2 0x33 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP2 ADD SWAP1 DUP1 DUP1 MLOAD PUSH1 0x40 MLOAD SWAP4 SWAP3 SWAP2 SWAP1 DUP5 PUSH5 0x100000000 DUP3 GT ISZERO PUSH2 0x53 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP4 DUP3 ADD SWAP2 POP PUSH1 0x20 DUP3 ADD DUP6 DUP2 GT ISZERO PUSH2 0x69 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP3 MLOAD DUP7 PUSH1 0x1 DUP3 MUL DUP4 ADD GT PUSH5 0x100000000 DUP3 GT OR ISZERO PUSH2 0x86 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP1 DUP4 MSTORE PUSH1 0x20 DUP4 ADD SWAP3 POP POP POP SWAP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP1 DUP4 DUP4 PUSH1 0x0 JUMPDEST DUP4 DUP2 LT ISZERO PUSH2 0xBA JUMPI DUP1 DUP3 ADD MLOAD DUP2 DUP5 ADD MSTORE PUSH1 0x20 DUP2 ADD SWAP1 POP PUSH2 0x9F JUMP JUMPDEST POP POP POP POP SWAP1 POP SWAP1 DUP2 ADD SWAP1 PUSH1 0x1F AND DUP1 ISZERO PUSH2 0xE7 JUMPI DUP1 DUP3 SUB DUP1 MLOAD PUSH1 0x1 DUP4 PUSH1 0x20 SUB PUSH2 0x100 EXP SUB NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP JUMPDEST POP PUSH1 0x40 MSTORE POP POP POP CALLER PUSH1 0x0 DUP1 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF MUL NOT AND SWAP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND MUL OR SWAP1 SSTORE POP DUP1 PUSH1 0x1 SWAP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 PUSH2 0x144 SWAP3 SWAP2 SWAP1 PUSH2 0x14B JUMP JUMPDEST POP POP PUSH2 0x1E8 JUMP JUMPDEST DUP3 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 SWAP1 PUSH1 0x1F ADD PUSH1 0x20 SWAP1 DIV DUP2 ADD SWAP3 DUP3 PUSH1 0x1F LT PUSH2 0x18C JUMPI DUP1 MLOAD PUSH1 0xFF NOT AND DUP4 DUP1 ADD OR DUP6 SSTORE PUSH2 0x1BA JUMP JUMPDEST DUP3 DUP1 ADD PUSH1 0x1 ADD DUP6 SSTORE DUP3 ISZERO PUSH2 0x1BA JUMPI SWAP2 DUP3 ADD JUMPDEST DUP3 DUP2 GT ISZERO PUSH2 0x1B9 JUMPI DUP3 MLOAD DUP3 SSTORE SWAP2 PUSH1 0x20 ADD SWAP2 SWAP1 PUSH1 0x1 ADD SWAP1 PUSH2 0x19E JUMP JUMPDEST JUMPDEST POP SWAP1 POP PUSH2 0x1C7 SWAP2 SWAP1 PUSH2 0x1CB JUMP JUMPDEST POP SWAP1 JUMP JUMPDEST JUMPDEST DUP1 DUP3 GT ISZERO PUSH2 0x1E4 JUMPI PUSH1 0x0 DUP2 PUSH1 0x0 SWAP1 SSTORE POP PUSH1 0x1 ADD PUSH2 0x1CC JUMP JUMPDEST POP SWAP1 JUMP JUMPDEST PUSH2 0x361 DUP1 PUSH2 0x1F7 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN INVALID PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x4 CALLDATASIZE LT PUSH2 0x36 JUMPI PUSH1 0x0 CALLDATALOAD PUSH1 0xE0 SHR DUP1 PUSH4 0x2E982602 EQ PUSH2 0x3B JUMPI DUP1 PUSH4 0x32AF2EDB EQ PUSH2 0xF6 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0xF4 PUSH1 0x4 DUP1 CALLDATASIZE SUB PUSH1 0x20 DUP2 LT ISZERO PUSH2 0x51 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 PUSH5 0x100000000 DUP2 GT ISZERO PUSH2 0x6E JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP3 ADD DUP4 PUSH1 0x20 DUP3 ADD GT ISZERO PUSH2 0x80 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP2 DUP5 PUSH1 0x1 DUP4 MUL DUP5 ADD GT PUSH5 0x100000000 DUP4 GT OR ISZERO PUSH2 0xA2 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST SWAP2 SWAP1 DUP1 DUP1 PUSH1 0x1F ADD PUSH1 0x20 DUP1 SWAP2 DIV MUL PUSH1 0x20 ADD PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 SWAP4 SWAP3 SWAP2 SWAP1 DUP2 DUP2 MSTORE PUSH1 0x20 ADD DUP4 DUP4 DUP1 DUP3 DUP5 CALLDATACOPY PUSH1 0x0 DUP2 DUP5 ADD MSTORE PUSH1 0x1F NOT PUSH1 0x1F DUP3 ADD AND SWAP1 POP DUP1 DUP4 ADD SWAP3 POP POP POP POP POP POP POP SWAP2 SWAP3 SWAP2 SWAP3 SWAP1 POP POP POP PUSH2 0x179 JUMP JUMPDEST STOP JUMPDEST PUSH2 0xFE PUSH2 0x1EC JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE DUP4 DUP2 DUP2 MLOAD DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP1 DUP4 DUP4 PUSH1 0x0 JUMPDEST DUP4 DUP2 LT ISZERO PUSH2 0x13E JUMPI DUP1 DUP3 ADD MLOAD DUP2 DUP5 ADD MSTORE PUSH1 0x20 DUP2 ADD SWAP1 POP PUSH2 0x123 JUMP JUMPDEST POP POP POP POP SWAP1 POP SWAP1 DUP2 ADD SWAP1 PUSH1 0x1F AND DUP1 ISZERO PUSH2 0x16B JUMPI DUP1 DUP3 SUB DUP1 MLOAD PUSH1 0x1 DUP4 PUSH1 0x20 SUB PUSH2 0x100 EXP SUB NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP JUMPDEST POP SWAP3 POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH1 0x0 DUP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND CALLER PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND EQ PUSH2 0x1D1 JUMPI PUSH2 0x1E9 JUMP JUMPDEST DUP1 PUSH1 0x1 SWAP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 PUSH2 0x1E7 SWAP3 SWAP2 SWAP1 PUSH2 0x28E JUMP JUMPDEST POP JUMPDEST POP JUMP JUMPDEST PUSH1 0x60 PUSH1 0x1 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV DUP1 PUSH1 0x1F ADD PUSH1 0x20 DUP1 SWAP2 DIV MUL PUSH1 0x20 ADD PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 SWAP3 SWAP2 SWAP1 DUP2 DUP2 MSTORE PUSH1 0x20 ADD DUP3 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV DUP1 ISZERO PUSH2 0x284 JUMPI DUP1 PUSH1 0x1F LT PUSH2 0x259 JUMPI PUSH2 0x100 DUP1 DUP4 SLOAD DIV MUL DUP4 MSTORE SWAP2 PUSH1 0x20 ADD SWAP2 PUSH2 0x284 JUMP JUMPDEST DUP3 ADD SWAP2 SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 SWAP1 JUMPDEST DUP2 SLOAD DUP2 MSTORE SWAP1 PUSH1 0x1 ADD SWAP1 PUSH1 0x20 ADD DUP1 DUP4 GT PUSH2 0x267 JUMPI DUP3 SWAP1 SUB PUSH1 0x1F AND DUP3 ADD SWAP2 JUMPDEST POP POP POP POP POP SWAP1 POP SWAP1 JUMP JUMPDEST DUP3 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 SWAP1 PUSH1 0x1F ADD PUSH1 0x20 SWAP1 DIV DUP2 ADD SWAP3 DUP3 PUSH1 0x1F LT PUSH2 0x2CF JUMPI DUP1 MLOAD PUSH1 0xFF NOT AND DUP4 DUP1 ADD OR DUP6 SSTORE PUSH2 0x2FD JUMP JUMPDEST DUP3 DUP1 ADD PUSH1 0x1 ADD DUP6 SSTORE DUP3 ISZERO PUSH2 0x2FD JUMPI SWAP2 DUP3 ADD JUMPDEST DUP3 DUP2 GT ISZERO PUSH2 0x2FC JUMPI DUP3 MLOAD DUP3 SSTORE SWAP2 PUSH1 0x20 ADD SWAP2 SWAP1 PUSH1 0x1 ADD SWAP1 PUSH2 0x2E1 JUMP JUMPDEST JUMPDEST POP SWAP1 POP PUSH2 0x30A SWAP2 SWAP1 PUSH2 0x30E JUMP JUMPDEST POP SWAP1 JUMP JUMPDEST JUMPDEST DUP1 DUP3 GT ISZERO PUSH2 0x327 JUMPI PUSH1 0x0 DUP2 PUSH1 0x0 SWAP1 SSTORE POP PUSH1 0x1 ADD PUSH2 0x30F JUMP JUMPDEST POP SWAP1 JUMP INVALID LOG2 PUSH5 0x6970667358 0x22 SLT KECCAK256 AND DIFFICULTY CHAINID 0x5F 0x5F PUSH20 0xDFD73A518B57770F5ADB27F025842235980D7A0F 0x4E ISZERO 0xB1 0xAC 0xB1 DUP15 PUSH5 0x736F6C6343 STOP SMOD STOP STOP CALLER ",
                                            "sourceMap": "33:623:0:-:0;;;186:160;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;301:10;293:5;;:18;;;;;;;;;;;;;;;;;;331:8;321:7;:18;;;;;;;;;;;;:::i;:::-;;186:160;33:623;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:::i;:::-;;;:::o;:::-;;;;;;;;;;;;;;;;;;;;;:::o;:::-;;;;;;;"
                                          },
                                          "deployedBytecode": {
                                            "immutableReferences": {},
                                            "linkReferences": {},
                                            "object": "608060405234801561001057600080fd5b50600436106100365760003560e01c80632e9826021461003b57806332af2edb146100f6575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610179565b005b6100fe6101ec565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561013e578082015181840152602081019050610123565b50505050905090810190601f16801561016b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146101d1576101e9565b80600190805190602001906101e792919061028e565b505b50565b606060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102845780601f1061025957610100808354040283529160200191610284565b820191906000526020600020905b81548152906001019060200180831161026757829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102cf57805160ff19168380011785556102fd565b828001600101855582156102fd579182015b828111156102fc5782518255916020019190600101906102e1565b5b50905061030a919061030e565b5090565b5b8082111561032757600081600090555060010161030f565b509056fea26469706673582212201644465f5f73dfd73a518b57770f5adb27f025842235980d7a0f4e15b1acb18e64736f6c63430007000033",
                                            "opcodes": "PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x4 CALLDATASIZE LT PUSH2 0x36 JUMPI PUSH1 0x0 CALLDATALOAD PUSH1 0xE0 SHR DUP1 PUSH4 0x2E982602 EQ PUSH2 0x3B JUMPI DUP1 PUSH4 0x32AF2EDB EQ PUSH2 0xF6 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0xF4 PUSH1 0x4 DUP1 CALLDATASIZE SUB PUSH1 0x20 DUP2 LT ISZERO PUSH2 0x51 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 PUSH5 0x100000000 DUP2 GT ISZERO PUSH2 0x6E JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP3 ADD DUP4 PUSH1 0x20 DUP3 ADD GT ISZERO PUSH2 0x80 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP2 DUP5 PUSH1 0x1 DUP4 MUL DUP5 ADD GT PUSH5 0x100000000 DUP4 GT OR ISZERO PUSH2 0xA2 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST SWAP2 SWAP1 DUP1 DUP1 PUSH1 0x1F ADD PUSH1 0x20 DUP1 SWAP2 DIV MUL PUSH1 0x20 ADD PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 SWAP4 SWAP3 SWAP2 SWAP1 DUP2 DUP2 MSTORE PUSH1 0x20 ADD DUP4 DUP4 DUP1 DUP3 DUP5 CALLDATACOPY PUSH1 0x0 DUP2 DUP5 ADD MSTORE PUSH1 0x1F NOT PUSH1 0x1F DUP3 ADD AND SWAP1 POP DUP1 DUP4 ADD SWAP3 POP POP POP POP POP POP POP SWAP2 SWAP3 SWAP2 SWAP3 SWAP1 POP POP POP PUSH2 0x179 JUMP JUMPDEST STOP JUMPDEST PUSH2 0xFE PUSH2 0x1EC JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE DUP4 DUP2 DUP2 MLOAD DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP1 DUP4 DUP4 PUSH1 0x0 JUMPDEST DUP4 DUP2 LT ISZERO PUSH2 0x13E JUMPI DUP1 DUP3 ADD MLOAD DUP2 DUP5 ADD MSTORE PUSH1 0x20 DUP2 ADD SWAP1 POP PUSH2 0x123 JUMP JUMPDEST POP POP POP POP SWAP1 POP SWAP1 DUP2 ADD SWAP1 PUSH1 0x1F AND DUP1 ISZERO PUSH2 0x16B JUMPI DUP1 DUP3 SUB DUP1 MLOAD PUSH1 0x1 DUP4 PUSH1 0x20 SUB PUSH2 0x100 EXP SUB NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP JUMPDEST POP SWAP3 POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH1 0x0 DUP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND CALLER PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND EQ PUSH2 0x1D1 JUMPI PUSH2 0x1E9 JUMP JUMPDEST DUP1 PUSH1 0x1 SWAP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 PUSH2 0x1E7 SWAP3 SWAP2 SWAP1 PUSH2 0x28E JUMP JUMPDEST POP JUMPDEST POP JUMP JUMPDEST PUSH1 0x60 PUSH1 0x1 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV DUP1 PUSH1 0x1F ADD PUSH1 0x20 DUP1 SWAP2 DIV MUL PUSH1 0x20 ADD PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 SWAP3 SWAP2 SWAP1 DUP2 DUP2 MSTORE PUSH1 0x20 ADD DUP3 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV DUP1 ISZERO PUSH2 0x284 JUMPI DUP1 PUSH1 0x1F LT PUSH2 0x259 JUMPI PUSH2 0x100 DUP1 DUP4 SLOAD DIV MUL DUP4 MSTORE SWAP2 PUSH1 0x20 ADD SWAP2 PUSH2 0x284 JUMP JUMPDEST DUP3 ADD SWAP2 SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 SWAP1 JUMPDEST DUP2 SLOAD DUP2 MSTORE SWAP1 PUSH1 0x1 ADD SWAP1 PUSH1 0x20 ADD DUP1 DUP4 GT PUSH2 0x267 JUMPI DUP3 SWAP1 SUB PUSH1 0x1F AND DUP3 ADD SWAP2 JUMPDEST POP POP POP POP POP SWAP1 POP SWAP1 JUMP JUMPDEST DUP3 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 SWAP1 PUSH1 0x1F ADD PUSH1 0x20 SWAP1 DIV DUP2 ADD SWAP3 DUP3 PUSH1 0x1F LT PUSH2 0x2CF JUMPI DUP1 MLOAD PUSH1 0xFF NOT AND DUP4 DUP1 ADD OR DUP6 SSTORE PUSH2 0x2FD JUMP JUMPDEST DUP3 DUP1 ADD PUSH1 0x1 ADD DUP6 SSTORE DUP3 ISZERO PUSH2 0x2FD JUMPI SWAP2 DUP3 ADD JUMPDEST DUP3 DUP2 GT ISZERO PUSH2 0x2FC JUMPI DUP3 MLOAD DUP3 SSTORE SWAP2 PUSH1 0x20 ADD SWAP2 SWAP1 PUSH1 0x1 ADD SWAP1 PUSH2 0x2E1 JUMP JUMPDEST JUMPDEST POP SWAP1 POP PUSH2 0x30A SWAP2 SWAP1 PUSH2 0x30E JUMP JUMPDEST POP SWAP1 JUMP JUMPDEST JUMPDEST DUP1 DUP3 GT ISZERO PUSH2 0x327 JUMPI PUSH1 0x0 DUP2 PUSH1 0x0 SWAP1 SSTORE POP PUSH1 0x1 ADD PUSH2 0x30F JUMP JUMPDEST POP SWAP1 JUMP INVALID LOG2 PUSH5 0x6970667358 0x22 SLT KECCAK256 AND DIFFICULTY CHAINID 0x5F 0x5F PUSH20 0xDFD73A518B57770F5ADB27F025842235980D7A0F 0x4E ISZERO 0xB1 0xAC 0xB1 DUP15 PUSH5 0x736F6C6343 STOP SMOD STOP STOP CALLER ",
                                            "sourceMap": "33:623:0:-:0;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;352:182;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:::i;:::-;;563:90;;;:::i;:::-;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;352:182;486:5;;;;;;;;;;472:19;;:10;:19;;;468:32;;493:7;;468:32;519:8;509:7;:18;;;;;;;;;;;;:::i;:::-;;352:182;;:::o;563:90::-;607:13;639:7;632:14;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;563:90;:::o;-1:-1:-1:-;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:::i;:::-;;;:::o;:::-;;;;;;;;;;;;;;;;;;;;;:::o"
                                          },
                                          "gasEstimates": {
                                            "creation": {
                                              "codeDepositCost": "173000",
                                              "executionCost": "infinite",
                                              "totalCost": "infinite"
                                            },
                                            "external": {
                                              "get_message()": "infinite",
                                              "set_message(string)": "infinite"
                                            }
                                          },
                                          "methodIdentifiers": {
                                            "get_message()": "32af2edb",
                                            "set_message(string)": "2e982602"
                                          }
                                        },
                                        "abi": [
                                          {
                                            "inputs": [
                                              {
                                                "internalType": "string",
                                                "name": "message_",
                                                "type": "string"
                                              }
                                            ],
                                            "stateMutability": "nonpayable",
                                            "type": "constructor"
                                          },
                                          {
                                            "inputs": [],
                                            "name": "get_message",
                                            "outputs": [
                                              {
                                                "internalType": "string",
                                                "name": "",
                                                "type": "string"
                                              }
                                            ],
                                            "stateMutability": "view",
                                            "type": "function"
                                          },
                                          {
                                            "inputs": [
                                              {
                                                "internalType": "string",
                                                "name": "message_",
                                                "type": "string"
                                              }
                                            ],
                                            "name": "set_message",
                                            "outputs": [],
                                            "stateMutability": "nonpayable",
                                            "type": "function"
                                          }
                                        ]
                                      }""";
     */
    public static void createAccount(String hederaAccount, String hederaPrivateKey) throws TimeoutException, ReceiptStatusException, PrecheckStatusException {
        //Grab your Hedera testnet account ID and private key
        AccountId myAccountId = AccountId.fromString(hederaAccount);
        PrivateKey myPrivateKey = PrivateKey.fromString(hederaPrivateKey);
        //Create your Hedera testnet client
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);
        // Generate a new key pair
        PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();
        //Create new account and assign the public key
        TransactionResponse newAccount = new AccountCreateTransaction()
                .setKey(newAccountPublicKey)
                .setInitialBalance(Hbar.from(10))
                .execute(client);
        // Get the new account ID
        AccountId newAccountId = newAccount.getReceipt(client).accountId;
        System.out.println("The new account ID is: " + newAccountId);
        System.out.println("The private key is: " + newAccountPrivateKey);
        //Check the new account's balance
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .execute(client);
        System.out.println("The new account balance is: " + accountBalance.hbars);
    }

    public static FileId storeSmartContractByteCodeOnHedera(String hederaAccount, String hederaPrivateKey, String jsonOutputFromSolidity, String network) throws TimeoutException, PrecheckStatusException, ReceiptStatusException, FileNotFoundException {
        AccountId myAccountId = AccountId.fromString(hederaAccount);
        PrivateKey myPrivateKey = PrivateKey.fromString(hederaPrivateKey);
        //Create your Hedera testnet client
        Client client;
        if (network.equals("TESTNET")) {
            client = Client.forTestnet();
        } else {
            client = Client.forMainnet();
        }
        //Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);

        //Import the compiled contract from the HelloHedera.json file
        Gson gson = new Gson();
        JsonObject jsonObject;

        // InputStream jsonStream = new FileInputStream("C:\\Users\\shakir.gusaroff\\Documents\\NetBeansProjects\\SmartContractFX\\src\\main\\java\\hedera22\\hackathon\\smartcontractfx\\HelloHedera.json");
        InputStream jsonStream = new ByteArrayInputStream(jsonOutputFromSolidity.getBytes());
        System.out.println("jsonstream = " + jsonStream);
        jsonObject = gson.fromJson(new InputStreamReader(jsonStream, StandardCharsets.UTF_8), JsonObject.class);
        System.out.println("jsonObject = " + jsonObject);
//Store the "object" field from the HelloHedera.json file as hex-encoded bytecode
        String object = jsonObject.getAsJsonObject("data").getAsJsonObject("bytecode").get("object").getAsString();
        byte[] bytecode = object.getBytes(StandardCharsets.UTF_8);

//Create a file on Hedera and store the hex-encoded bytecode
        FileCreateTransaction fileCreateTx = new FileCreateTransaction()
                //Set the bytecode of the contract
                .setContents(bytecode);

//Submit the file to the Hedera test network signing with the transaction fee payer key specified with the client
        TransactionResponse submitTx = fileCreateTx.execute(client);

//Get the receipt of the file create transaction
        TransactionReceipt fileReceipt = submitTx.getReceipt(client);

//Get the file ID from the receipt
        FileId bytecodeFileId = fileReceipt.fileId;

//Log the file ID
        System.out.println("The smart contract bytecode file ID is " + bytecodeFileId);
        //  if (bytecodeFileId == null) {throw new PrecheckStatusException();}

        return bytecodeFileId;
//v2 Hedera Java SDK

    }

    public static ContractId deployHederaSmartContract(String hederaAccount, String hederaPrivateKey, FileId fileId, String network, String constructorValue) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        AccountId myAccountId = AccountId.fromString(hederaAccount);
        PrivateKey myPrivateKey = PrivateKey.fromString(hederaPrivateKey);
        //Create your Hedera testnet client
        Client client;
        if (network.equals("TESTNET")) {
            client = Client.forTestnet();
        } else {
            client = Client.forMainnet();
        }
        client.setOperator(myAccountId, myPrivateKey);

        // Instantiate the contract instance
        ContractCreateTransaction contractTx = new ContractCreateTransaction()
                //Set the file ID of the Hedera file storing the bytecode
                .setBytecodeFileId(fileId)
                //Set the gas to instantiate the contract
                .setGas(100_000)
                //Provide the constructor parameters for the contract
                .setConstructorParameters(new ContractFunctionParameters().addString(constructorValue));

//Submit the transaction to the Hedera test network
        TransactionResponse contractResponse = contractTx.execute(client);

//Get the receipt of the file create transaction
        TransactionReceipt contractReceipt = contractResponse.getReceipt(client);

        //if (  contractReceipt..status != ResponseCodeEnum.SUCCESS) {}
//Get the smart contract ID
        ContractId newContractId = contractReceipt.contractId;

//Log the smart contract ID
        System.out.println("The smart contract ID is " + newContractId);
        return newContractId;
//v2 Hedera Java SDK
    }

    public static String callContractNoParamQuery(String hederaAccount, String hederaPrivateKey, ContractId contractId, String network, String functionNaame) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        AccountId myAccountId = AccountId.fromString(hederaAccount);
        PrivateKey myPrivateKey = PrivateKey.fromString(hederaPrivateKey);
        //Create your Hedera testnet client
        Client client;
        if (network.equals("TESTNET")) {
            client = Client.forTestnet();
        } else {
            client = Client.forMainnet();
        }
        client.setOperator(myAccountId, myPrivateKey);

//Query the contract for the contract message
        ContractCallQuery contractCallQuery = new ContractCallQuery()
                //Set ID of the contract to query
                .setContractId(contractId)
                //Set the gas to execute the contract call
                .setGas(100_000)
                //Set the contract function
                .setFunction(functionNaame)
                //Set the query payment for the node returning the request
                //This value must cover the cost of the request otherwise will fail 
                .setQueryPayment(new Hbar(2));

        //Submit the query to a Hedera network
        ContractFunctionResult result = contractCallQuery.execute(client);

//Get the updated message
        String message = result.getString(0);

        //String message2 = The contract updated message: " + message2;
        return "Contract function returnes message: " + message;
    }

    public static String callContractNoParamTransaction(String hederaAccount, String hederaPrivateKey, ContractId contractId, String network, String functionNaame) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        AccountId myAccountId = AccountId.fromString(hederaAccount);
        PrivateKey myPrivateKey = PrivateKey.fromString(hederaPrivateKey);
        //Create your Hedera testnet client
        Client client;
        if (network.equals("TESTNET")) {
            client = Client.forTestnet();
        } else {
            client = Client.forMainnet();
        }
        client.setOperator(myAccountId, myPrivateKey);

//Query the contract for the contract message
        //Create the transaction to update the contract message
        ContractExecuteTransaction contractExecTx = new ContractExecuteTransaction()
                //Set the ID of the contract
                .setContractId(contractId)
                //Set the gas for the call
                .setGas(100_000)
                //Set the function of the contract to call
                .setFunction(functionNaame);

        //contractExecTx.setf
//Submit the transaction to a Hedera network and store the response
        TransactionResponse submitExecTx = contractExecTx.execute(client);
        TransactionReceipt receipt = submitExecTx.getReceipt(client);

//Confirm the transaction was executed successfully 
        System.out.println("The transaction status is" + receipt.status);
        //String message2 = The contract updated message: " + message2;
        return "Contract returnes message: " + receipt.status;
    }

    public static String callContractOneParamTransaction(String hederaAccount, String hederaPrivateKey, ContractId contractId, String network, String functionName, String functionParameter) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        AccountId myAccountId = AccountId.fromString(hederaAccount);
        PrivateKey myPrivateKey = PrivateKey.fromString(hederaPrivateKey);
        //Create your Hedera testnet client
        Client client;
        if (network.equals("TESTNET")) {
            client = Client.forTestnet();
        } else {
            client = Client.forMainnet();
        }
        client.setOperator(myAccountId, myPrivateKey);

        //Create the transaction to update the contract message
        ContractExecuteTransaction contractExecTx = new ContractExecuteTransaction()
                //Set the ID of the contract
                .setContractId(contractId)
                //Set the gas for the call
                .setGas(100_000)
                //Set the function of the contract to call
                .setFunction(functionName, new ContractFunctionParameters().addString(functionParameter));

        //contractExecTx.setf
//Submit the transaction to a Hedera network and store the response
        TransactionResponse submitExecTx = contractExecTx.execute(client);

//Get the receipt of the transaction
        TransactionReceipt receipt2 = submitExecTx.getReceipt(client);

//Confirm the transaction was executed successfully 
        System.out.println("The transaction status is" + receipt2.status);
        return "The transaction status is: " + receipt2.status;

    }

}
