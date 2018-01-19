package circuit.ru.xmn.circuit.model.common

interface Editable {
    var currentState: EditableState
    fun change(state: EditableState){
        currentState = state
        internalChange(state)
    }
    fun internalChange(state: EditableState)
}

abstract class CommonEditable: Editable {
    override var currentState = NormalState as EditableState
}

interface EditableParent : Editable {
    val editableChildes: MutableList<Editable>

    override fun change(state: EditableState) {
        super.change(state)
        when (state) {
            is NormalState -> {
                editableChildes.forEach { it.change(NormalState) }
            }
            is EditState -> {
                editableChildes.forEach { it.change(EditState) }
            }
        }
    }

    fun addEditableChild(child: Editable) {
        child.change(currentState)
        editableChildes.add(child)
    }
}

class CommonEditableParent() : EditableParent {
    override fun internalChange(state: EditableState) {
        print(state)
    }
    override var currentState: EditableState = NormalState
    override val editableChildes: MutableList<Editable> = ArrayList()
}

sealed class EditableState
object NormalState : EditableState()
object EditState : EditableState()