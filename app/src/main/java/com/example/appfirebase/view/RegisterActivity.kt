package com.example.appfirebase.view


import AutenticacaoController
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.example.appfirebase.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var ctrl : AutenticacaoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSalvar.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val senha = binding.txtSenha.text.toString()
            ctrl = AutenticacaoController()
            ctrl.criarUsuario(email, senha) { sucesso, erro ->
                if (sucesso) {
                    Toast.makeText(this, "Usuário criado com sucesso",
                        Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao criar usuário: " +
                            erro.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
