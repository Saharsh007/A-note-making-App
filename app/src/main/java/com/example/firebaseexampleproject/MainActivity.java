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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    //private DocumentReference noteRef = db.collection("NOTEBOOK").document("My first note");   WAS USING IT EARLIER FOR SINGLE DOCUMENT
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
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e != null){
                        return;
                    }

                String data = "";
                for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                    Note note = documentSnapshots.toObject(Note.class);         //ONE PARTICULAR DOCUMENT QUERIED
                    note.setDocumentId(documentSnapshots.getId());
                    String documentID = note.getDocumentId();
                    String title =  note.getTitle();
                    String description = note.getDescription();
                    data += "\n\nID:"+ documentID + "Title:"+ title + "\nDescription:" + description;
                }
                textViewData.setText(data);

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
        notebookRef.add(note); //ADDED USING RANDOM ID AS NAME OF DOUCMENT , NAME OF COLLECTION IS "NOTEBOOK"
    }


    //////FUNCTION FOR LOAD BUTTON
    public  void loadNotes(View v) {
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";
                        for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            Note note = documentSnapshots.toObject(Note.class);         //ONE PARTICULAR DOCUMENT QUERIED
                           note.setDocumentId(documentSnapshots.getId());
                           String documentID = note.getDocumentId();
                            String title =  note.getTitle();
                            String description = note.getDescription();
                            data += "\n\nID:"+ documentID + "Title:"+ title + "\nDescription:" + description;
                        }
                        textViewData.setText(data);

                    }
                });
    }
    //////FUNCTION FOR LOAD BUTTON



}
