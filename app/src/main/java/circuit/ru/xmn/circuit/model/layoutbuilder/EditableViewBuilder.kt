package circuit.ru.xmn.circuit.model.layoutbuilder


class EditableViewBuilder(val viewBuilder: ViewBuilder) : ViewBuilder by viewBuilder, Stateful {
    override fun change(state: State) {
        when (state) {
            is NormalState -> {
            }
            is EditState -> {
            }
        }
    }

}