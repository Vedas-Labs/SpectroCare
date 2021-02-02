package com.vedas.spectrocare.PatientChat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.vedas.spectrocare.Constants;
import com.vedas.spectrocare.PatientChat.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ViewController {
  Context context;
  public static ViewController myObj;

  public static ViewController getInstance() {
    if (myObj == null) {
      myObj = new ViewController();
    }
    return myObj;
  }

  public void fillContent(Context context1) {
    context = context1;

  }

  public void uploadFileAction(File file, String fileType, String roomId,String userID,String messageId,byte[] fileArray) {
    // {"roomID":"APNTIDqEQ4EV6K","userID":"PIDH4g1L"}
    Log.e("arraysize", "call" + fileArray.length + file.getName());
    String fileID = "file" + String.valueOf(getRandomInteger(1000, 10));
    FileInfo fileInfo = new FileInfo();
    fileInfo.setId(fileID);
    fileInfo.setName(file.getName());
    fileInfo.setOffset(0);
    fileInfo.setSize(fileArray.length);
    fileInfo.setData(fileArray);
    fileInfo.setType(fileType);
    fileInfo.setMessageID(messageId);
    fileInfo.setRoomID(roomId);
    fileInfo.setUserID(userID);
    fileInfo.setMessageID("msg_" + String.valueOf(getRandomInteger(100, 10)));
    fileInfo.setMessageType("SingleImage");
    Log.e("uploadFileAction", "call" + fileInfo.getData().length+fileID);
    SocketIOHelper.getInstance().sendFileToSocket(Constants.SpectroSocketEvents.SliceUpload.toString(), fileInfo);
  }

  public int getRandomInteger(int maximum, int minimum) {
    return ((int) (Math.random() * (maximum - minimum))) + minimum;
  }
}



