package com.example.appfirebase

import AutenticacaoController
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appfirebase.databinding.ActivityMainBinding
import com.example.appfirebase.ui.theme.AppFirebaseTheme
import com.example.appfirebase.view.CategoriaActivity
import com.example.appfirebase.view.EsqueceuSenhaActivity
import com.example.appfirebase.view.ItensActivity
import com.example.appfirebase.view.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ctrl: AutenticacaoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener {
            val email = binding.txtEmail.text.toString();
            val senha = binding.txtSenha.text.toString();
            ctrl = AutenticacaoController()
            ctrl.login(email, senha) { sucesso, erro ->
                if (sucesso) {
                    val intent = Intent(this, CategoriaActivity::class.java)
                    startActivity(intent)
                } else {
                    println("Erro no login: $erro")
                }
            }

        }

        binding.txtCadastrar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.txtEsqueceuSenha.setOnClickListener {
            val intent = Intent(this, EsqueceuSenhaActivity::class.java)
            startActivity(intent)
        }
    }
}
