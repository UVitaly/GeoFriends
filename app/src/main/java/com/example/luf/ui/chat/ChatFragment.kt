package com.example.luf.ui.chat


import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.luf.R
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private var adapter: FirebaseListAdapter<ChatMessage>? = null
    private val mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
    private var query = mFirebaseDatabaseReference.child("User")
    private var nameUser: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatViewModel =
            ViewModelProvider(this).get(ChatViewModel::class.java)
        val root = inflater.inflate(R.layout.activity_chat, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab : Button = requireView().findViewById(R.id.btn_send)

        fab.setOnClickListener {
            val input = requireActivity().findViewById(R.id.textField) as EditText
            val eventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(FirebaseAuth.getInstance().currentUser!=null)
                    {
                        for (ds in dataSnapshot.children)
                        {
                            if(ds.key==FirebaseAuth.getInstance().currentUser!!.uid)
                            {
                                nameUser = ds.child("fullName").value.toString()
                            }
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            if(query.database!=null)
                query.addListenerForSingleValueEvent(eventListener)

            FirebaseDatabase.getInstance().reference.child("Chat").push().setValue( ChatMessage( input.text.toString(), nameUser) )
            input.setText("")
        }
        displayChatMessages()
    }
    private fun displayChatMessages()
    {
        val listOfMessages: ListView = requireActivity().findViewById(R.id.list_of_messages) as ListView

        adapter = object : FirebaseListAdapter<ChatMessage>(
            requireActivity(), ChatMessage::class.java,
            R.layout.message, FirebaseDatabase.getInstance().reference.child("Chat")
        ) {
            override fun populateView(v: View, model: ChatMessage, position: Int) {
                // Get references to the views of message.xml
                val messageText = v.findViewById<View>(R.id.message_text) as TextView
                val messageUser = v.findViewById<View>(R.id.message_user) as TextView
                val messageTime = v.findViewById<View>(R.id.message_time) as TextView

                // Set their text
                messageText.text = model.messageText
                messageUser.text = model.messageUser

                // Format the date before showing it
                messageTime.text = DateFormat.format(
                    "dd-MM-yyyy (HH:mm:ss)",
                    model.messageTime
                )
            }
        }

        listOfMessages.setAdapter(adapter)
    }

}