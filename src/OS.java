import java.io.*;
import java.util.*;

public class OS {
    public String[] Memory;
    public Queue<String[]> queue;

    public OS() {
        queue = new ArrayDeque<String []>();
    }

    public String readFile(String fileName, int start, int end) throws IOException {
        //read the file and return it as a string
        if (readMemory(fileName, start, end)!=null){
            fileName = readMemory(fileName, start, end);
        }
        String res = "";
        try{
            FileReader file = new FileReader("src/"+fileName);
            BufferedReader br = new BufferedReader(file);
            String st;
            while ((st = br.readLine()) != null){
                res = res + st + "\n";
            }
            if (res==null){
                res = "File not found!";
            }
            return res;
        }
        catch (Exception e){
            return "File not found!";
        }

    }

    public void writeFile(String fileName, String data, int start, int end) throws IOException {
        if (readMemory(fileName, start, end)!=null){
            fileName = readMemory(fileName, start, end);
        }
        if (readMemory(data, start, end)!= null){
            data = readMemory(data, start, end);
        }


        FileWriter fr = new FileWriter("src/"+fileName);
        BufferedWriter br = new BufferedWriter(fr);
        String d = data;
        br.write(d);
        br.close();
        fr.close();
    }

    public void writeOutput(String print, int start, int end){
        String x = print;
        String v= readMemory(print, start, end);
        if(v!=null){
            x=print+"="+v;
        }
        System.out.println(x);
    }

    public String readInput(){
        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        return userInput;
    }

    public String readMemory(String x, int start, int end){
        for(int i=start; i<=end; i++){

            if((Memory[i].split("=")[0]).equals(x)){
                if((Memory[i].split("=")).length==1){
                    return null;
                }
                return Memory[i].split("=")[1];
            }
        }
        return null;
    }

    public void writeMemory(String assigned, String assignee, int start, int end){
        for (int i=start; i<=end; i++){
            if((Memory[i].split("=")[0]).equals(assigned)){
                Memory[i] = assigned+"="+assignee;
            }
        }
    }

    public void assign(String a, String b, int start, int end){

        String x = b;
        String v = readMemory(b, start, end);
        if(v!=null){
            x = v;
        }
        writeMemory(a, x, start, end);
    }

    public void add(String assigned, String assignee, int start, int end){
        String x = assignee;
        String v = readMemory(assignee, start, end);
        if(readMemory(assigned, start, end)==null){
            try {
                writeFile(assigned, "0", start, end);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(v!=null){
            x= v;
        }
        int y = Integer.parseInt(readMemory(assigned, start, end))+Integer.parseInt(x);
        writeMemory(assigned, y+"", start, end);
    }

    public void parser(String program, int start, int end) throws IOException {

        File file = new File("src/"+program);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null){
            String[] line = st.split(" ");
            Stack <String> s = new Stack<>();
            for (int j = 0 ; j < line.length ; j++){
                s.push(line[j]);
            }
            while(!s.isEmpty()){
                String b = null ;
                String a = null ;
                String inst = null ;
                b = s.pop();
                if (b.equals("input")){
                    b = readInput() ;
                    s.push(b);
                    continue;
                }
                if (s.peek().equals("readFile")){
                    inst = s.pop();
                    s.push(readFile(b, start, end));
                    continue;
                }
                if (s.peek().equals("print")){
                    inst = s.pop();
                    writeOutput(b, start, end);
                    continue;
                }
                a = s.pop();
                if (s.peek().equals("assign")){
                    inst = s.pop();
                    assign(a, b, start, end);
                    continue;
                }
                if (s.peek().equals("add")){
                    inst = s.pop();
                    add(a, b, start, end);
                    continue;
                }
                if (s.peek().equals("writeFile")){
                    inst = s.pop();
                    writeFile(a, b, start, end);
                    continue;
                }


            }// end of stack


        }// end of loop of program

    }// end of parse method

    public void sequentialMilestone1() throws IOException {

        while (!queue.isEmpty()){
            String[] pcb = queue.remove();

            int start = Integer.parseInt(pcb[3].split(": ")[1].split("-")[0]);
            int end = Integer.parseInt(pcb[3].split(": ")[1].split("-")[1]);
            int noInstruction = Integer.parseInt(pcb[4].split(": ")[1]);
            int pc = Integer.parseInt(pcb[2].split(": ")[1]);
            pcb[1]= "State: Running";
            Memory[start+1]= "State: Running";
            for (int i = 0 ;i <noInstruction ; i++) {
                if (pc < noInstruction) {
                    String instruction = Memory[start + 5 + pc];
                    lineParser(instruction, start, end);
                    pc++;
                    pcb[2] = "Program Counter: " + pc;
                    Memory[start+2]="Program Counter: " + pc;
                }
                else {
                    break;
                }
            }
            if (pc <noInstruction){
                Memory[start+1]= "State: Ready";
                pcb[1]= "State: Ready";
                queue.add(pcb);
            }
            else{
                Memory[start+1]= "State: Done";
                pcb[1]= "State: Done";
            }


        }// end of queue

    }

    public void programsRunOne(ArrayList<String> programs) throws IOException {
        int linesNeeded = 0;
        for(int i=0; i<programs.size(); i++){
            Set<String> variables = new LinkedHashSet<>();
            File file = new File("src/"+programs.get(i));

            BufferedReader br = new BufferedReader(new FileReader(file));
            String st = "";
            linesNeeded+=5;
            while ((st = br.readLine()) != null){
                String[] line = st.split(" ");
                for(int j=0; j< line.length; j++){
                    if(line[j].equals("print")){
                        break;
                    }
                    if(!line[j].equals("assign") && !line[j].equals("add") && !line[j].equals("readFile") && !line[j].equals("writeFile")  && !line[j].equals("input")){
                        variables.add(line[j]);
                    }
                }
                linesNeeded++;
            }
            linesNeeded+= variables.size();
        }
        Memory = new String[linesNeeded];
    }

    public void programsRunTwo(ArrayList<String> programs) throws IOException {
        int progStart = 0;
        int progEnd = 0;
        for(int i=0; i<programs.size(); i++){
            Set<String> variables = new HashSet<>();
            int insCounter = 0;
            File file = new File("src/"+programs.get(i));
            String name = (programs.get(i).substring(8, programs.get(i).length()-4));
            Memory[progEnd++] = "ID: Process " + name;
            Memory[progEnd++] = "State: Ready";
            Memory[progEnd++] = "Program Counter: 0";
            Memory[progEnd++] = "Memory Boundaries: "+progStart+"-";
            Memory[progEnd++] = "Number of Instructions: ";
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st = "";
            while ((st = br.readLine()) != null){
                String[] line = st.split(" ");
                for(int j=0; j< line.length; j++){
                    if(line[j].equals("print")){
                        break;
                    }
                    if(!line[j].equals("assign") && !line[j].equals("add") && !line[j].equals("readFile") && !line[j].equals("writeFile") && !line[j].equals("input")){
                        variables.add(line[j]);
                    }
                }
                Memory[progEnd++] = st;
                insCounter++;
            }

            Iterator<String> it = variables.iterator();
            while (it.hasNext()){
                Memory[progEnd++] = it.next()+"=";
            }
            progEnd--;
            Memory[progStart+3]+= progEnd;
            Memory[progStart+4] += insCounter;
            String[] PCB = new String[5];
            PCB[0] = Memory[progStart];
            PCB[1] = Memory[progStart+1];
            PCB[2] = Memory[progStart+2];
            PCB[3] = Memory[progStart+3];
            PCB[4] = Memory[progStart+4];
            queue.add(PCB);
            progStart = ++progEnd;
        }
    }

    public void lineParser(String st, int start, int end) throws IOException {
        String[] line = st.split(" ");
        Stack <String> s = new Stack<>();
        for (int j = 0 ; j < line.length ; j++){
            s.push(line[j]);
        }
        while(!s.isEmpty()){
            String b = null ;
            String a = null ;
            String inst = null ;
            b = s.pop();
            if (b.equals("input")){
                b = readInput() ;
                s.push(b);
                continue;
            }
            if (s.peek().equals("readFile")){
                inst = s.pop();
                s.push(readFile(b, start, end));
                continue;
            }
            if (s.peek().equals("print")){
                inst = s.pop();
                writeOutput(b, start, end);
                continue;
            }
            a = s.pop();
            if (s.peek().equals("assign")){
                inst = s.pop();
                assign(a, b, start, end);
                continue;
            }
            if (s.peek().equals("add")){
                inst = s.pop();
                add(a, b, start, end);
                continue;
            }
            if (s.peek().equals("writeFile")){
                inst = s.pop();
                writeFile(a, b, start, end);
                continue;
            }
        }// end of stack
    }

    public void roundRobin2 () throws IOException {

        while (!queue.isEmpty()){
            String[] pcb = queue.remove();

            int start = Integer.parseInt(pcb[3].split(": ")[1].split("-")[0]);
            int end = Integer.parseInt(pcb[3].split(": ")[1].split("-")[1]);
            int noInstruction = Integer.parseInt(pcb[4].split(": ")[1]);
            int pc = Integer.parseInt(pcb[2].split(": ")[1]);
            pcb[1]= "State: Running";
            Memory[start+1]= "State: Running";
            for (int i = 0 ;i <2 ; i++) {
                if (pc < noInstruction) {
                    String instruction = Memory[start + 5 + pc];
                    lineParser(instruction, start, end);
                    pc++;
                    pcb[2] = "Program Counter: " + pc;
                    Memory[start+2]="Program Counter: " + pc;
                }
                else {
                    break;
                }
            }
            if (pc <noInstruction){
                Memory[start+1]= "State: Ready";
                pcb[1]= "State: Ready";
                queue.add(pcb);
            }
            else{
                Memory[start+1]= "State: Done";
                pcb[1]= "State: Done";
            }


        }// end of queue

    }

    public void run(ArrayList<String> programs) throws IOException {
        programsRunOne(programs);
        programsRunTwo(programs);
        roundRobin2();
    }

    public static void main(String[] args) {
        OS x = new OS();
//        milestone 1
//        ArrayList<String> programs = new ArrayList<String>();
//        programs.add("Program 1.txt");
//        programs.add("Program 2.txt");
//        programs.add("Program 3.txt");
//        try {
//            x.programsRunOne(programs);
//            x.programsRunTwo(programs);
//            x.sequentialMilestone1();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        milestone 2
        ArrayList<String> programs = new ArrayList<String>();
        programs.add("Program 1.txt");
        programs.add("Program 2.txt");
        programs.add("Program 3.txt");

        try {
            x.run(programs);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0; i<x.Memory.length; i++){
            System.out.println(x.Memory[i]);
        }

        System.out.println("------------------------------");
        while(!x.queue.isEmpty()){

            String [] lol = x.queue.remove();
            for (int j = 0 ; j < lol.length; j++){
                System.out.println(lol[j]);
            }
        }

    }// end of main

}
