package com.vedas.spectrocare.PatientChat;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.vedas.spectrocare.Constants;
import com.vedas.spectrocare.PatientChat.ChatApplication;
import com.vedas.spectrocare.PatientChat.ChatDataController;
import com.vedas.spectrocare.PatientChat.FileInfo;
import com.vedas.spectrocare.PatientChat.MessagesListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOHelper {
    public static SocketIOHelper myObj;
    public ednResponseInterface ednResponse;
    Context context;
    public Socket socket;
    int chunkSize = 100000;
    ArrayList<FileInfo> filesUploading;

    public static SocketIOHelper getInstance() {
        if (myObj == null) {
            myObj = new SocketIOHelper();
        }
        return myObj;
    }
    public void attachResponse(ednResponseInterface ednResponseInterface) {
        if(ednResponse!=null){
            ednResponse=null;
        }
        ednResponse = ednResponseInterface;
    }

    public void fillContent(Context context1) {
        context = context1;
        filesUploading = new ArrayList<>();
    }
    public void socketConnect(){
        ChatApplication app = (ChatApplication) context.getApplicationContext();
        socket = app.getSocket();
        socket.connect();
        Log.e("soctt","Id : "+socket.id());

    }
    public void socketDisConnected(){
        if(socket.connected()) {
            socket.disconnect();
        }
    }
    public void listenEvents(){
        socket.on(Constants.SpectroSocketEvents.RequestSliceUpload.toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.e("RequestSliceUpload", " chatmessage" + data.toString());
                try {
                    int offset=data.getInt("currentSlice");
                    String fileId=data.getString("fileID");
                    // FileInfo fileInfo=getFileWithID(fileId);
                    FileInfo fileInfo = getFileWithID(fileId);
                    Log.e("offsett","::"+offset);

//                    Log.e("testing", ""+offset+fileInfo.getId()+fileInfo.getName()+fileInfo.getData().length);
                    if (fileInfo!=null){
                        fileInfo.setOffset(offset);
                        sendEvent(Constants.SpectroSocketEvents.SliceUpload.toString(), fileInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        socket.on(Constants.SpectroSocketEvents.EndUpload.toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.e("EndUpload", " chat message" + data.toString());
                try {
                    // socket.on(Constants.SpectroSocketEvents.RequestSliceUpload.toString()
                    // socket.off(Constants.SpectroSocketEvents.RequestSliceUpload.toString());
                    Log.e("hjgjh","hgjf"+2+3);
                    // JSONObject response=data.getJSONObject("response");
                    Log.e("ResponseEND", " chat message" + data.toString());
                    if(ednResponse!=null) {
                        ednResponse.endFileResponse(data);
                    }

                   /* Gson gson = new Gson();
                    String object = data.getJSONObject("messageData").toString();
                    MessagesListModel messageModel = gson.fromJson(object, MessagesListModel.class);*/

                    String fileId=data.getString("fileID");
                    delete(fileId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on(Constants.SpectroSocketEvents.UploadError.toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                Log.e("UploadError", " chat message" + response.toString());
                try {
                    //JSONObject response=data.getJSONObject("response");
                    String fileId=response.getString("fileID");
                    Log.e("ErrorFileID", " chatmessage" + fileId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void sendFileToSocket(String eventName, FileInfo fileInfo) {

        filesUploading.add(fileInfo);
        sendEvent(eventName, fileInfo);
    }

    private void sendEvent(String eventName, FileInfo fileInfo1) {
        FileInfo fileInfo=new FileInfo(fileInfo1);
        int length = fileInfo.getData().length;
        int offset = fileInfo.getOffset() * chunkSize;
        Log.e("sendEvent","call"+fileInfo.getData().length+offset);
        if (offset < length) {
            Log.e("offset<length","call"+fileInfo.getData().length);

            int thisChunkSize = ((length - offset) > chunkSize) ? chunkSize : (length - offset);
            byte[] chunk = Arrays.copyOfRange(fileInfo.getData(), offset, offset+thisChunkSize);
            Log.e("chunk","call"+chunk.length);
            FileInfo chunkFileInfo = fileInfo;
            chunkFileInfo.setData(chunk);

            Gson gson = new Gson();
            String jsonString = gson.toJson(chunkFileInfo);
            Log.e("firstsendEvent","call"+jsonString.toString());
            socket.emit(eventName, jsonString);
        }
    }

    private void  delete(String fileID) {
        Log.e("UploadFilesBefore", ""+filesUploading.size());
        for (int i=0; i<filesUploading.size();i++) {
            FileInfo fileInfo = filesUploading.get(i);
            if (fileID.equals(fileInfo.getId())) {
                filesUploading.remove(fileInfo);
            }
        }
       /* filesUploading = filesUploading.filter {
            !$0.id.contains(fileID)
        }*/
        Log.e("UploadFilesAfterDelete", ""+filesUploading.size());
    }

    public FileInfo getFileWithID(String fileID)   {
        Log.e("filesUploading", ""+filesUploading.size());
        for (int i=0; i<filesUploading.size();i++) {
            FileInfo fileInfo = filesUploading.get(i);
            if (fileID .equals(fileInfo.getId())) {
                Log.e("getFileWithID", ""+fileInfo.getId()+fileInfo.getData().length);
                return fileInfo;
            }
        }
        return null;
    }
    public interface ednResponseInterface {
        void endFileResponse(JSONObject endFileResponse);
    }
}


/*
package com.vedas.spectrocare.PatientChat;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.vedas.spectrocare.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOHelper {
    public static SocketIOHelper myObj;
    Context context;
    public Socket socket;
    int chunkSize = 100000;
    ArrayList<FileInfo> filesUploading;

    public static SocketIOHelper getInstance() {
        if (myObj == null) {
            myObj = new SocketIOHelper();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
        filesUploading = new ArrayList<>();
    }
    public void socketConnect(){
        ChatApplication app = (ChatApplication) context.getApplicationContext();
        socket = app.getSocket();
        socket.connect();
    }
    public void socketDisConnected(){
        if(socket.connected()) {
            socket.disconnect();
        }
    }
    public void listenEvents(){
        socket.on(Constants.SpectroSocketEvents.RequestSliceUpload.toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.e("RequestSliceUpload", " chatmessage" + data.toString());
                try {
                    int offset=data.getInt("currentSlice");
                    String fileId=data.getString("fileID");
                    FileInfo fileInfo=getFileWithID(fileId);
                    Log.e("testing", ""+offset+fileInfo.getId()+fileInfo.getName()+fileInfo.getData().length);
                        fileInfo.setOffset(offset);
                        sendEvent(Constants.SpectroSocketEvents.SliceUpload.toString(), fileInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        socket.on(Constants.SpectroSocketEvents.EndUpload.toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.e("EndUpload", " chat message" + data.toString());
                try {
                   // JSONObject response=data.getJSONObject("response");
                    Log.e("ResponseEND", " chat message" + data.toString());
                    String fileId=data.getString("fileID");
                    delete(fileId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on(Constants.SpectroSocketEvents.UploadError.toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                Log.e("UploadError", " chat message" + response.toString());
                try {
                    //JSONObject response=data.getJSONObject("response");
                    String fileId=response.getString("fileID");
                    Log.e("ErrorFileID", " chatmessage" + fileId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void sendFileToSocket(String eventName, FileInfo fileInfo) {

        filesUploading.add(fileInfo);
        sendEvent(eventName, fileInfo);
    }

    private void sendEvent(String eventName, FileInfo fileInfo1) {
        FileInfo fileInfo=new FileInfo(fileInfo1);
        int length = fileInfo.getData().length;
        int offset = fileInfo.getOffset() * chunkSize;
        Log.e("sendEvent","call"+fileInfo.getData().length+offset);
        if (offset < length) {
            Log.e("offset<length","call"+fileInfo.getData().length);

            int thisChunkSize = ((length - offset) > chunkSize) ? chunkSize : (length - offset);
            byte[] chunk = Arrays.copyOfRange(fileInfo.getData(), offset, offset+thisChunkSize);
            Log.e("chunk","call"+chunk.length);
            FileInfo chunkFileInfo = fileInfo;
            chunkFileInfo.setData(chunk);

            Gson gson = new Gson();
            String jsonString = gson.toJson(chunkFileInfo);
            Log.e("firstsendEvent","call"+jsonString.toString());
            socket.emit(eventName, jsonString);
        }
    }

    private void  delete(String fileID) {
        Log.e("UploadFilesBefore", ""+filesUploading.size());
        for (int i=0; i<filesUploading.size();i++) {
            FileInfo fileInfo = filesUploading.get(i);
            if (fileID.equals(fileInfo.getId())) {
                filesUploading.remove(fileInfo);
            }
        }
       */
/* filesUploading = filesUploading.filter {
            !$0.id.contains(fileID)
        }*//*

        Log.e("UploadFilesAfterDelete", ""+filesUploading.size());
    }

    public FileInfo getFileWithID(String fileID)   {
        Log.e("filesUploading", ""+filesUploading.size());
        for (int i=0; i<filesUploading.size();i++) {
            FileInfo fileInfo = filesUploading.get(i);
            if (fileID .equals(fileInfo.getId())) {
                Log.e("getFileWithID", ""+fileInfo.getId()+fileInfo.getData().length);
                return fileInfo;
            }
        }
        return null;
    }
}


*/
/*
class SocketIOHelper {

    static let shared = SocketIOHelper()
    var socket: SocketIOClient!
    let chunkSize = 100000
    var filesUploading = [FileInfo]()
    let manager = SocketManager(socketURL: URL(string: "http://127.0.0.1:3000")!, config: [.log(true), .compress])

    private init() {
        socket = manager.defaultSocket
    }

    func connectSocket(completion: @escaping(Bool) -> () ) {
        disconnectSocket()
        socket.on(clientEvent: .connect) {[weak self] (data, ack) in
            print("socket connected")
            self?.socket.removeAllHandlers()
            self?.listenEvents()
            completion(true)
        }
        socket.connect()

    }

    func disconnectSocket() {
        socket.removeAllHandlers()
        socket.disconnect()
        print("socket Disconnected")
    }

    func checkConnection() -> Bool {
        if socket.manager?.status == .connected {
            return true
        }
        return false

    }
    func listenEvents()  {

        socket.on(SpectroSocketEvents.requestSliceUpload.rawValue) {data, ack in

            guard let response = data[0] as? Dictionary<String, Any> else { return }
            print("ResponseDATA",response)
            guard let offset = response["currentSlice"] as? Int , let fileID = response["fileID"] as? String else {
                return
            }
            guard var fileInfo =  self.getFileWithID(fileID: fileID) else {
                return
            }
            fileInfo.offset = offset
            self.sendEvent(eventName: SpectroSocketEvents.sliceUpload, fileInfo:fileInfo )

            //ack.with("Got your currentAmount", "dude")
        }
        socket.on(SpectroSocketEvents.endUpload.rawValue) { [self]data, ack in
            guard let response = data[0] as? Dictionary<String, Any> else { return }
            print("---------------------------------------------")
            print("ResponseEND",response)
            print("---------------------------------------------")
            guard let fileID = response["fileID"] as? String else {
                return
            }
            self.delete(fileID: fileID)
        }

        socket.on(SpectroSocketEvents.uploadError.rawValue) { [self]data, ack in
            guard let response = data[0] as? Dictionary<String, Any> else { return }
            print("---------------------------------------------")
            print("Error ResponseEND",response)
            print("---------------------------------------------")
            guard let fileID = response["fileID"] as? String else {
                return
            }
            //  self.delete(fileID: fileID)
        }

    }

    func delete(fileID: String) {
        print("Upload Files Before Delete", filesUploading.count)
        filesUploading = filesUploading.filter {
            !$0.id.contains(fileID)
        }
        print("Upload Files After Delete", filesUploading.count)
    }

    func sendFileToSocket(eventName:SpectroSocketEvents,fileInfo: FileInfo) {

        filesUploading.append(fileInfo)
        sendEvent(eventName: eventName, fileInfo: fileInfo)
    }

    private func sendEvent(eventName:SpectroSocketEvents,fileInfo: FileInfo)  {

        let length = fileInfo.data.count
        var offset = fileInfo.offset*chunkSize

        if(offset < length){
            let thisChunkSize = ((length - offset) > chunkSize) ? chunkSize : (length - offset);
            let chunk = Array(fileInfo.data[offset..<offset + thisChunkSize])
            //offset += thisChunkSize;
            var chunkFileInfo  = fileInfo
            chunkFileInfo.data = chunk
            do {
                //https://stackoverflow.com/questions/33186051/swift-convert-struct-to-json
                let jsonEncoder = JSONEncoder()
                let jsonData = try jsonEncoder.encode(chunkFileInfo)
                self.socket.emit(eventName.rawValue, jsonData)


            } catch {
                print(error.localizedDescription)
            }
        }
    }

    func getFileWithID(fileID:String) -> FileInfo?  {
        for i in 0..<filesUploading.count {
            let fileInfo = filesUploading[i];
            if fileID == fileInfo.id {
                return fileInfo
            }
        }
        return nil
    }
}
*/

