package JemuGB;

import CPU.CPU;
import CPU.UnknownOpCodeException;
import Memory.MemoryManagementUnit;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;

/**
 * Created by devin on 4/16/16.
 */
public class GameBoy {
    private static final File BIOS_FILE = new File("./BIOS.bin");

    private MemoryManagementUnit memoryManagementUnit = new MemoryManagementUnit();
    private CPU cpu = new CPU(memoryManagementUnit);

    public void initializeRAM() throws Exception {
        if (!BIOS_FILE.exists()) {
            throw new FileNotFoundException("BIOS.bin could not be found. If you have" +
                    " not done so already please place a copy of the JemuGB.JemuGB's BIOS in " +
                    "the folder containing JemuGB.");
        }

        // Load the BIOS
        InputStream fileInputStream = new FileInputStream(BIOS_FILE);
        byte[] biosImage = IOUtils.toByteArray(fileInputStream);
        memoryManagementUnit.loadImage(biosImage, 0x0000);
    }

    public void powerOn() throws Exception, UnknownOpCodeException {
        initializeRAM();

        while (true) {
            int cyclesPast = cpu.process();
        }
    }

    public static void main(String[] args) {
        try {
            GameBoy gameBoy = new GameBoy();
            gameBoy.powerOn();
        } catch (Exception exception) {
            String errorMessage = exception.getMessage();
            System.out.println(errorMessage);
        } catch (UnknownOpCodeException exception) {
            String errorMessage = exception.getMessage();
            System.out.println(errorMessage);
        }
    }
}