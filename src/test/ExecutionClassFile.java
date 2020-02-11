package test;

public class ExecutionClassFile extends FIORIAutomation{

	public static void main(String[] args) throws Exception {
		invokeBrowser();
		validateGraphData();
		closeInstance();
	}
}
