import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;


public class TestMain {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testMain() throws Exception {
        final File tempInput = tempFolder.newFile("input.csv");
        final File tempOutput = tempFolder.newFile("output.csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempInput));
        bw.write("a,\"b,c\",c");
        bw.close();

        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream(String.format(
                "%s\n%s\n,\n+", tempInput.getPath(), tempOutput.getPath()).getBytes()
        );
        System.setIn(in);

        Main.main(new String[]{});

        BufferedReader br = new BufferedReader(new FileReader(tempOutput.getPath()));
        assertEquals("1+3+1", br.readLine());
        br.close();
    }

    @Test
    public void testSplitAndCount() throws IOException {
        final File tempFile = tempFolder.newFile("input.csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write("a,\"b,c\",c");
        bw.close();

        String regex = String.format("%1$s(?=(?:[^%2$s]*[%2$s][^%2$s]*[%2$s])*[^%2$s]*$)", ",", "\"''");
        Pattern pattern = Pattern.compile(regex);

        List<String> res = Main.splitAndCount(tempFile.getPath(), "+", pattern);
        assertEquals(res.get(0), "1+3+1");
    }

}
