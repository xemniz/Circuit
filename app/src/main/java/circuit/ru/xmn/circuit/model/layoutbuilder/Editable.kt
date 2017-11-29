package circuit.ru.xmn.circuit.model.layoutbuilder

interface Editable {
    fun change(state: EditableState)
}

interface EditableParent : Editable {
    val editableChildes: List<Editable>
    fun parentChange(state: EditableState)

    override fun change(state: EditableState) {
        parentChange(state)
        when (state) {
            is NormalState -> {
                editableChildes.forEach { it.change(NormalState) }
            }
            is EditState -> {
                editableChildes.forEach { it.change(EditState) }
            }
        }
    }
}

sealed class EditableState
object NormalState : EditableState()
object EditState : EditableState()