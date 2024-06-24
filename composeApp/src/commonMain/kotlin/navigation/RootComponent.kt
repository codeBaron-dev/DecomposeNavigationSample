package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    @ExperimentalDecomposeApi
    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.ScreenA -> Child.ScreenA(ScreenAComponent(
                componentContext = context,
                onNavigateToScreenB = { name -> navigation.pushNew(Configuration.ScreenB(name))}
            ))

            is Configuration.ScreenB -> Child.ScreenB(
                ScreenBComponent(
                    componentContext = context,
                    name = configuration.name,
                    onBack = { navigation.pop()}
                )
            )
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.ScreenA,
        handleBackButton = true,
        childFactory = ::createChild
    )

    sealed class Child {
        data class ScreenA(val component: ScreenAComponent) : Child()
        data class ScreenB(val component: ScreenBComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object ScreenA : Configuration()

        @Serializable
        data class ScreenB(val name: String) : Configuration()
    }
}