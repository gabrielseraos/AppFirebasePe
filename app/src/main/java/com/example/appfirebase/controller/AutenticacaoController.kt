import com.google.firebase.auth.FirebaseAuth

class AutenticacaoController {

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message
                    onResult(false, errorMessage)
                }
            }



    }

    fun criarUsuario(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message
                    onResult(false, errorMessage)
                }
            }
    }
    fun esqueceuSenha(email: String, onResult: (Boolean, String?) -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message
                    onResult(false, errorMessage)
                }
            }
    }

    fun usuarioAutenticado(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser?.email.toString()
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
    }

    fun idUsuarioAutenticado(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser?.uid.toString()
    }


}
