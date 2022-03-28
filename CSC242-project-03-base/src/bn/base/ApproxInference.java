package bn.base;
import bn.core.RandomVariable;
import bn.core.Value;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;
import org.xml.sax.SAXException;

import java.lang.Math;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ApproxInference {


    public Distribution RejectSampling(RandomVariable X, Assignment e, BayesianNetwork bn, int n) {

        Distribution Q = new Distribution(X);

        Domain domain = (Domain) X.getDomain();

        for(Value v : domain) {
            Q.put(v,0.0);
        }
        for(int i  = 0 ; i < n ; i++) {
            Assignment assign = new Assignment();
            ArrayList<RandomVariable> result  = (ArrayList<RandomVariable>) bn.getVariablesSortedTopologically();

            for(RandomVariable var: result) {
                double random = Math.random();
              //  System.out.println(random);
                Domain x = (Domain) var.getDomain();
                for(Value v: x) {
                    assign.put(var,v);
                    random = random+ bn.getProbability(var,assign);
                    if (random > 1.0) {
                        break;
                    }
                }
            }
            if(assign.containsAll(e)) {
                Q.put(assign.get(X),Q.get(assign.get(X))+1);
            }

        }
        Q.normalize();

        return Q;


    }




    public  static void main(String [] args) throws IOException, ParserConfigurationException, SAXException {

        int n = Integer.valueOf(args[0]);
        System.out.println(n);
        BayesianNetwork network = new BayesianNetwork();

        String file = args[1];
        if(file.contains(".bif")) {
            File f= new File(file);
            BIFParser parser = new BIFParser(new FileInputStream(f));
            network = (BayesianNetwork) parser.parseNetwork();
        }
        else {
            XMLBIFParser parser = new XMLBIFParser();
            network = (BayesianNetwork) parser.readNetworkFromFile(file);
        }

        RandomVariable X = network.getVariableByName(args[2]);
        Assignment e = new bn.base.Assignment();
        int i = 3;
        int length = args.length;

        while (i < length) {
            RandomVariable var = network.getVariableByName(args[i]);
            Value v1 = new StringValue(args[i+1]);
            e.put(var,v1);
            i = i + 2;
        }
        ApproxInference approxInference = new ApproxInference();
        if(n*10 > Integer.MAX_VALUE) {
            System.out.println(approxInference.RejectSampling(X,e,network,Integer.MAX_VALUE));
        }
        else {
            System.out.println(approxInference.RejectSampling(X,e,network,n*10));
        }


//        System.out.println("hello");
//        BayesianNetwork network = new BayesianNetwork();
//        XMLBIFParser parser = new XMLBIFParser();
//        network = (BayesianNetwork) parser.readNetworkFromFile("C:\\Users\\Jonathan\\Downloads\\CSC242-project-03-examples\\src\\bn\\examples\\aima-alarm.xml");
//
//        RandomVariable X = network.getVariableByName("B");
//        Assignment e = new bn.base.Assignment();
//      //  System.out.println(BooleanValue.TRUE);
//        e.put(network.getVariableByName("J"),new StringValue("true"));
//        e.put(network.getVariableByName("M"),new StringValue("true"));
//
//      //  System.out.println(network);
//        ApproxInference approxInference = new ApproxInference();
//        System.out.println(approxInference.RejectSampling(X,e,network,10000));


    }

}
