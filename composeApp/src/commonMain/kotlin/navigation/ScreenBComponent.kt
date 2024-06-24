package navigation

import com.arkivanov.decompose.ComponentContext

class ScreenBComponent(
    val name: String,
    componentContext: ComponentContext,
    private val onBack: () -> Unit
) : ComponentContext by componentContext {

     fun goBack() {
         onBack()
     }
}