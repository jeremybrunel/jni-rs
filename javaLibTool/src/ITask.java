package src;

public interface ITask {
    public byte[] perform(String libPath, byte[] jsonSchemaInputsAsBytes);
}
