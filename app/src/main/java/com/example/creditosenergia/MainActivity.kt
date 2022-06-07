package com.example.creditosenergia

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val TAG = "MyActivity"

    //funções para esconder o teclado
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    //funções para esconder o teclado
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    //funções para esconder o teclado
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // não lembro para que serve isso
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "teste")

        /* linkagem dos campos da tela para variaveis do programa

        Apesar de ser uma variável, os campos iniciais de 03 e 103 devem ficar fixos durante o mês
        por isso no diagrama da tela eu adicionei os valores do mês corrente, porém se precisar, é possível
        mudar os valores clicando nos campos, essa mudança não será guardada na próxima abertura do app
        que voltará aos valores que foram definidos na tela.

         */
        val credito_inicial = findViewById<EditText>(R.id.tela_credito_inicial)
        val atual_03 = findViewById<EditText>(R.id.tela_final_03)
        val atual_103 = findViewById<EditText>(R.id.tela_final_103)
        val inicial_03 = findViewById<EditText>(R.id.tela_inicial_03)
        val inicial_103 = findViewById<EditText>(R.id.tela_inicial_103)
        val texto = findViewById<TextView>(R.id.texto_resultado)
        val b1 = findViewById<Button>(R.id.bt_calcular)
        val b2 = findViewById<Button>(R.id.bt_apagar)

        // ação do botão de calcular
        b1.setOnClickListener {
            try {
                hideKeyboard()
                val credito_inicial = credito_inicial.text.toString().toInt() //Créditos. tem que converter para int por algum motivo
                val inicial_03 = inicial_03.text.toString().toInt() //03 mês passado. tem que converter para int por algum motivo
                val inicial_103 = inicial_103.text.toString().toInt() //103 mês passado. tem que converter para int por algum motivo
                val final_03 = atual_03.text.toString().toInt() //03 atual. tem que converter para int por algum motivo
                val final_103 = atual_103.text.toString().toInt() //103 atual. tem que converter para int por algum motivo

                // cálculo dos valores
                val resultado03 = final_03 - inicial_03
                val resultado103 = final_103 - inicial_103
                val diferença = resultado103 - resultado03 // o resultado 103 tem que vir primeiro pra fazer a conta de menos
                var credito_final = 0 //pode ser um resultado negativo ou positivo
                texto.requestFocus()

                /*
                se a diferença for um número menor que -50 significa que a produção foi menor que o consumo,
                e os créditos vão ser descontados, como a companhia cobra 50kw todos os meses de qualquer jeito
                só vai começar a descontar o que passar de 50
                ex.: se a diferença for 51kw só vai ser descontado dos créditos 1kw
                 */
                if (diferença < -50){
                    credito_final = credito_inicial + diferença + 50
                    texto.setText("Consumo: $resultado03 kW\nProdução: $resultado103 kW\nDiferença: $diferença kW\nCréditos: $credito_final kW\nDiferença é maior que 50kW,\nvai começar a descontar dos créditos")

                /*
                se a diferença for um número maior que -50 e menor de 0 significa que a produção foi menor que o consumo,
                mas todos os meses a companhia cobra 50kw de qualquer jeito
                então os créditos não serão descontados
                ex.: Se a diferença for 49kw não vai descontar dos créditos.
                 */
                } else if (diferença < 0){
                    credito_final = credito_inicial
                    texto.setText("Consumo: $resultado03 kW\nProdução: $resultado103 kW\nDiferença: $diferença kW\nProdução negativa\nporém ainda abaixo dos -50kW, não irá descontar dos créditos")
                }

                //Se for um número maior que zero esse valor será somado aos créditos
                else {
                    credito_final = credito_inicial + diferença
                    texto.setText("Consumo: $resultado03 kW\nProdução: $resultado103 kW\nDiferença: $diferença kW\nCréditos: $credito_final kW\nSaldo Positivo!")
                }

            // função para não deixar campos em branco
            } catch (e: Exception) {

                atual_03.setError("É preciso colocar um número em todos os campos")
                atual_03.requestFocus()
                atual_103.setError("É preciso colocar um número em todos os campos")
                atual_103.requestFocus()
            }
        }

        // ação do botão de apagar tudo
        b2.setOnClickListener {

            texto.setText("Resultado")
            atual_03.setText("")
            atual_103.setText("")


        }


    }


}


