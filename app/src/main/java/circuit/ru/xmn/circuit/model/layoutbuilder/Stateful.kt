package circuit.ru.xmn.circuit.model.layoutbuilder

interface Stateful {
    fun change(state: State)
}

sealed class State
object NormalState: State()
object EditState: State()