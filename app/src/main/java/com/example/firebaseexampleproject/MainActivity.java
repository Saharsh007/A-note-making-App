package com.example.firebaseexampleproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String  KEY_DESCRIPTION = "description";
    EditText editTextTitle;
    EditText editTextDescription;
    private TextView textViewData;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("NOTEBOOK").document("My first note");
    CollectionReference notebookRef  = db.collection("NOTEBOOK");
   // private ListenerRegistration noteListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextTitle = findViewById(R.id.edit_text_title);
        textViewData = findViewById(R.id.text_view_date);
    }

    //////USING IT TO DISPLAY STUFFS ON APP START
    @Override
    protected  void onStart(){
        super.onStart();
       noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this, "ERROR WHILE LOADING", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,e.toString());
                    return;
                }
                if(documentSnapshot.exists()){
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();

                    // Map<String,Object> map  = documentSnapshot.getData();         //another way to do it

                    textViewData.setText("Title is "+ title+ "\nDescr   iption is "+description);
                }else{
                    //ONCE DELETED EVERYTHING TEXT WILL BECOME BLANK SO
                    textViewData.setText(""); //SET TEXT TO BLACK
                }
            }
        });
    }
      ///////Onstart is called when app starts


    /////////////////THIS IS TO STOP THE DATA FETCH WHEN APP IS STOPPED
//    @Override
//    protected void onStop(){
//        super.onStop();
//        noteListener.remove();
//    }
    /////WHY NOT USING IT?
    /////WE CAN USE "THIS" IN START ACTIVITY WHICH WILL DETACH THE LISTENER WHEN START IS OVER SO THIS WILL NO LONGER BE REQUIRED
    /////////////////THIS IS TO STOP THE DATA FETCH WHEN APP IS STOPPED



    public void addNote(View v){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Note note = new Note(title,description);

        /////////////////////UPLOADING TASK
        noteRef.set(note)           //MAIN CODE
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "ULOADED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(  new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "DIDN'T UPLAOD TO DATABASE", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,e.toString());

                    }
                });
        /////////////////UPLOADING DONE
    }


    //////FUNCTION FOR LOAD BUTTON
    public  void loadNote(View v){
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Note note = documentSnapshot.toObject(Note.class);
                            String title = note.getTitle();
                            String description = note.getDescription();

                           // Map<String,Object> map  = documentSnapshot.getData();         //another way to do it

                            textViewData.setText("Title is "+ title+ "\nDescription is "+description);
                        } else{
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "SOME ERROR CANNOT DISPLAY", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,e.toString());
                    }
                });
    }
    //////FUNCTION FOR LOAD BUTTON



    /////FUNCTION FOR UPDATE DESCRIPTION BUTTON
    public void updateDescription(View v) {
        String description = editTextDescription.getText().toString();
       Map<String,Object> note = new HashMap<>();
        note.put(KEY_DESCRIPTION,description);
        noteRef.set(note, SetOptions.merge()); //NOTEREF IS A FIREBASE REFERENCE  SET TO notebook/my first note
        //EVEN IF WE DETELE DATE ON FIREBASE MANUALLY ,I WON'T MATTER AS THIS CAN MANAGE EVERYTHING
        //EVEN THIS CAN BE USED TO DO THE SAME WORK AS noteRef.set(note, SetOptions.merge());
       // noteRef.update(KEY_DESCRIPTION,description);

    }


    public void deleteDescription(View v) {
//        Map<String,Object> note = new HashMap<>();
//        note.put(KEY_DESCRIPTION, FieldValue.delete());
//        noteRef.update(note);

        // or simply
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public void deleteNote(View v) {

        noteRef.delete();
        ////THE  FOLDERS STILL EXIST BUT THE INSIDE OF NOTEBOOK/My first document is gone
        /////THE FOLDER ALSO GETS DELETED IF THERE ARE NO DOCUMENTS IN IT
    }


}
