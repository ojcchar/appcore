package seers.appcore.pipeline;

@FunctionalInterface
public interface PipelineCommand<I, O> {

    O execute(I value);

    default <R> PipelineCommand<I, R> pipe(PipelineCommand<O, R> sourceCommand) {
        return inputValue -> sourceCommand.execute(execute(inputValue));
    }

    static <I, O> PipelineCommand<I, O> of(PipelineCommand<I, O> sourceCommand) {
        return sourceCommand;
    }
}
