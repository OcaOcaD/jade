package test;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import net.sf.clipsrules.jni.*;

public class Challenge2 extends Agent {

    Environment clips;

  protected void setup() {
      try {
           clips = new Environment();
      } catch (Exception e){}
     
    addBehaviour(new TellBehaviour());
    addBehaviour(new AskBehaviour());
  } 

  private class TellBehaviour extends Behaviour {

    boolean tellDone = false;

    public void action() {
        try{
          
          // // Market
          // clips.load("C:/Users/donid/Documents/agent-repo/clips/market/templates.clp");
          // clips.load("C:/Users/donid/Documents/agent-repo/clips/market/rules.clp");
          // clips.load("C:/Users/donid/Documents/agent-repo/clips/market/facts.clp");

          // // Person
          // clips.load("C:/Users/donid/Documents/agent-repo/clips/persons/load-persons.clp");
          // clips.load("C:/Users/donid/Documents/agent-repo/clips/persons/load-persons-rules.clp");

          // // Products
          // clips.load("C:/Users/donid/Documents/agent-repo/clips/market/load-prod-cust.clp");
          // clips.load("C:/Users/donid/Documents/agent-repo/clips/market/load-prodcust-rules.clp");

          clips.build("(deftemplate product (slot part-number) (multislot name) (slot category) (slot price))");
          clips.build("(deffacts products (product (part-number 1234) (name USB Memory) (category storage) (price 9.99)) (product (name Amplifier) (category electronics) (part-number 2341) (price 399.99)) (product (name Speakers) (category electronics) (part-number 23241) (price 19.99)) (product (name iPhone 7) (category smartphone) (part-number 3412) (price 99.99)) (product (name Samsung Edge 7) (category smartphone) (part-number 34412) (price 88.99)) )");
          clips.build("(defrule my-rule11 (product (name ?n) (price 9.99)) => (printout  t \"Customer name found:\"  ?n crlf ))");
          
        
        }catch (Exception e){}

        tellDone = true;
       
    } 
    
    public boolean done() {
      if (tellDone)
        return true;
      else
	return false;
    }
   

  }    // END of inner class ...Behaviour


  private class AskBehaviour extends Behaviour {

    boolean askDone = false;

    public void action() {
        try{
         //clips.eval("(reset)");
          System.out.println("FACTS: ");
          clips.eval("(facts)"); 
          System.out.println("RULES: ");
          clips.eval("(rules)");

          clips.run();
        }catch(Exception e){}
       askDone = true;
        

    } 
    
    public boolean done() {
      if (askDone)
        return true;
      else
	      return false;
    }
   
    public int onEnd() {
      myAgent.doDelete();
      return super.onEnd();
    } 
  }    // END of inner class ...Behaviour
}
