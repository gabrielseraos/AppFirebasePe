package com.example.appfirebase.view

import AutenticacaoController
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appfirebase.R
import com.example.appfirebase.databinding.ActivityEsqueceuSenhaBinding

class EsqueceuSenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueceuSenhaBinding
    private lateinit var ctrl : AutenticacaoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEsqueceuSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEnviar.setOnClickListener {
            val email = binding.txtEmail.text.toString()

            ctrl = AutenticacaoController()
            ctrl.esqueceuSenha(email) { sucesso, erro ->
                if (sucesso) {
                    Toast.makeText(this,"Um e-mail de redefinição de senha foi enviado para " +
                            "o seu endereço de e-mail.",
                        Toast.LENGTH_LONG).show()
                    finish()

                } else {
                    Toast.makeText(this,
                        "Falha ao enviar e-mail de redefinição de senha. " +
                                "Verifique se o endereço de e-mail é válido.",
                        Toast.LENGTH_LONG).show()
                }
            }

        }

    }
}
