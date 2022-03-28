package bn.base;




import bn.core.RandomVariable;
import bn.core.Value;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExactInference {

    public Distribution ENUMERATION_ASK(RandomVariable X, Assignment e, BayesianNetwork bn) {
        Distribution Q = new Distribution(X);

        Domain domain = (Domain) X.getDomain();


        for(Value v: domain) {
            Assignment copy = e.copy();
            copy.put(X,v);
            ArrayList<RandomVariable> result  = (ArrayList<RandomVariable>) bn.getVariablesSortedTopologically();
            Double val = ENUMERATE_ALL(bn,result,copy);
            Q.put(v,val);

        }
        Q.normalize();

        return Q;
    }

    public Double ENUMERATE_ALL(BayesianNetwork bn, ArrayList<RandomVariable> Xlist, Assignment e ) {
        double ans = 1.0;
        if(Xlist.isEmpty()) {
            return ans;
        }
        RandomVariable First  = Xlist.remove(0);

        if(e.containsKey(First)) {
            return bn.getProbability(First,e) * ENUMERATE_ALL(bn,Xlist,e);
        }
        else {
            ans = 0.0;
            for(Value v : First.getDomain()) {
                Assignment copy = e.copy(); // Save
                copy.put(First,v);
                ArrayList<RandomVariable> result = new ArrayList<RandomVariable>(Xlist);
                ans = ans + bn.getProbability(First,copy) * ENUMERATE_ALL(bn,result,copy);
            }
        }
return ans;
    }

    public static void main(String [] jonnysarg) throws IOException, ParserConfigurationException, SAXException {


//            String file = jonnysarg[0];
//        BayesianNetwork network = new BayesianNetwork();
//
//            if(file.contains(".bif")) {
//                File f= new File(jonnysarg[0]);
//                BIFParser parser = new BIFParser(new FileInputStream(f));
//                network = (BayesianNetwork) parser.parseNetwork();
//            }
//            else {
//                XMLBIFParser parser = new XMLBIFParser();
//                network = (BayesianNetwork) parser.readNetworkFromFile(jonnysarg[0]);
//            }
//            //Query Vraible
//
//        RandomVariable X = network.getVariableByName(jonnysarg[1]);
//        Assignment e = new bn.base.Assignment();
//
//
//        int i = 2;
//        int n = jonnysarg.length;
//
//        while (i <= n) {
//            RandomVariable var = network.getVariableByName(jonnysarg[i]);
//            Value v1 = new StringValue(jonnysarg[i+1]);
//            e.put(var,v1);
//            i = i + 2;
//
//        }
//
//        ExactInference hello = new ExactInference();
//
//        Distribution ans = hello.ENUMERATION_ASK(X,e,network);
//
//        System.out.println(ans);

        System.out.println("hello");
        BayesianNetwork network = new BayesianNetwork();
        XMLBIFParser parser = new XMLBIFParser();
        network = (BayesianNetwork) parser.readNetworkFromFile("C:\\Users\\Jonathan\\Downloads\\CSC242-project-03-examples (1)\\src\\bn\\examples\\aima-alarm.xml");

        RandomVariable X = network.getVariableByName("B");
        Assignment e = new bn.base.Assignment();
        System.out.println(BooleanValue.TRUE);
        e.put(network.getVariableByName("J"),new StringValue("true"));
        e.put(network.getVariableByName("M"),new StringValue("true"));
        ExactInference hello = new ExactInference();
        System.out.println(hello.ENUMERATION_ASK(X,e,network));
        System.out.println("P(B|j,m) = \\alpha <0.00059224,0.0014919> ~= <0.284,0.716>");

        Assignment s  = new bn.base.Assignment();
        network = (BayesianNetwork) parser.readNetworkFromFile("C:\\Users\\Jonathan\\Downloads\\CSC242-project-03-examples (1)\\src\\bn\\examples\\aima-wet-grass.xml");
        X = network.getVariableByName("R");
        s.put(network.getVariableByName("S"),new StringValue("true"));
        System.out.println(hello.ENUMERATION_ASK(X,s,network));


        System.out.println("P(Rain|Sprinkler=true) = <0.3,0.7>");
    }






}
