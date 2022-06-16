/*
@author Mustafa Bulut
@since 09.06.2021
 */


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class hmw20170808030{
}




class Main {

    public static void main(String[] args) {
        String myStr;
        if (args.length == 0) myStr = null;
        else myStr = args[0];
        if (myStr == null || myStr.isEmpty())
            myStr = "input.txt";
        Compiler compiler = new Compiler(myStr);
        compiler.start();
    }
}

class Instruction {
    private int p;
    private String inst;
    private int v;

    public Instruction(int P, String I, int V) {
        this.p = P;
        this.inst = I;
        this.v = V;
    }

    public Instruction(int position, String instruction) {
        this.p = position;
        this.inst = instruction;
        v = 0;
    }
    public void setP(byte p) {
        this.p = p;
    }
    public String getInst() {
        return inst;
    }
    public int getP() {
        return p;
    }
    public void setInst(String inst) {
        this.inst = inst;
    }
    public int getV() {
        return v;
    }
    public void setV(byte v) {
        this.v = v;
    }
}

class Compiler extends Thread {
    private int[] memory = new int[256];
    private Stack<Integer> stack = new Stack<Integer>();
    private int AC = 0;
    private int PC = 0;
    private int AX = 0;
    private String str = null;

    private boolean ex = true;
    private int flag;
    private boolean jmp = false;

    public Compiler(String path) {
        FileHandler.setFilePath(path);
    }

    @Override
    public void run() {
        super.run();
        executeInstruction();
        while (ex) {
            executeInstruction();
        }
    }

    private void executeInstruction() {
        Instruction instruction =
                FileHandler.readInstructionFromFile(PC);
        if (instruction != null) {
            switch (instruction.getInst()) {
                case "START":
                    START();
                    break;

                case "LOAD":
                    LOAD(instruction.getV());
                    break;
                case "LOADM":
                    LOADM(instruction.getV());
                    break;
                case "STORE":
                    STORE(instruction.getV());
                    break;
                case "CMPM":
                    CMPM(instruction.getV());
                    break;
                case "CJMP":
                    CJMP(instruction.getV());
                    break;
                case "JMP":
                    JMP(instruction.getV());
                    break;
                case "ADD":
                    ADD(instruction.getV());
                    break;
                case "ADDM":
                    ADDM(instruction.getV());
                    break;
                case "SUBM":
                    SUBM(instruction.getV());
                    break;
                case "SUB":
                    SUB(instruction.getV());
                    break;
                case "MUL":
                    MUL(instruction.getV());
                    break;
                case "MULM":
                    MULM(instruction.getV());
                    break;
                case "PUSH":
                    PUSH(instruction.getV());
                    break;
                case "DISP":
                    DISP();
                    break;
                case "DASC":
                    DASC();
                    break;
                case "HALT":
                    HALT();
                    break;
                case "LOADI":
                    LOADI();
                    break;
                case "STOREI":
                    STOREI();
                    break;
                case "SWAP":
                    SWAP();
                    break;
                case "POP":
                    POP();
                    break;
                case "RETURN":
                    RETURN();
                    break;
            }
            if (!jmp) PC++;
            jmp = false;
        }
    }

    private void START() {
        ex = true;
    }

    private void LOAD(int value) {
        AC = value;
    }

    private void LOADM(int memoryValue) {
        AC = memory[memoryValue];
    }

    private void STORE(int memoryValue) {
        memory[memoryValue] = AC;
    }

    private void CMPM(int memoryValue) {
        flag = Integer.compare(AC, memory[memoryValue]);
    }

    private void CJMP(int value) {
        if (flag > 0) {
            PC = value;
            jmp = true;
        }
    }

    private void JMP(int value) {
        PC = value;
        jmp = true;
    }

    private void ADD(int value) {
        AC += value;
    }

    private void ADDM(int memoryValue) {
        AC += memory[memoryValue];
    }

    private void SUBM(int memoryValue) {
        AC -= memory[memoryValue];
    }

    private void SUB(int value) {
        AC -= value;
    }

    private void MUL(int value) {
        AC *= value;
    }

    private void MULM(int memoryValue) {
        AC *= memory[memoryValue];
    }

    private void DISP() {
        System.out.print(AC);
    }

    private void DASC() {
        str = Character.toString((char) AC);
        System.out.print(str);
    }

    private void HALT() {
        ex = false;
    }

    private void LOADI() {
        AC = memory[AX];
    }

    private void STOREI() {
        memory[AX] = AC;
    }

    private void SWAP() {
        memory[255] = AC;
        AC = AX;
        AX = memory[255];
    }

    private void PUSH(int value) {
        stack.push(value);
    }

    private void POP() {

        AC = stack.pop();
    }

    private void RETURN() {
        PC = stack.pop();
        jmp = true;
    }
}

class FileHandler {
    private static String filePathAsString = "program.txt";

    public static Instruction[] readInstructionsFromFile() {
        try {
            List<String> lines =
                    Files.readAllLines(Paths.get(filePathAsString));
            List<Instruction> instructions = new ArrayList<>();
            for (String line : lines) {
                instructions.add(convertStringToInstruction(line));
            }
            return instructions.toArray(new Instruction[0]);
        } catch (IOException e) {
            return new Instruction[0];
        }
    }

    public static void setFilePath(String path) {
        filePathAsString = path;
    }


    public static Instruction readInstructionFromFile(int position) {
        try {
            return
                    convertStringToInstruction(Files.readAllLines(Paths.get(filePathAsString)).get(position));
        } catch (IOException e) {
            return null;
        }
    }

    private static Instruction convertStringToInstruction(String line) {
        String[] instruction = line.split(" ");
        if (instruction.length == 2) return new
                Instruction(Integer.parseInt(instruction[0]), instruction[1]);
        else if (instruction.length == 3) {
            return new
                    Instruction(Integer.parseInt(instruction[0]), instruction[1], Integer.parseInt(instruction[2]));
        } else return null;
    }
}
