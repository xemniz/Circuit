package circuit.ru.xmn.circuit.model.layoutbuilder

interface Editable {
    fun change(state: EditableState)
}

sealed class EditableState
object NormalState: EditableState()
object EditState: EditableState()