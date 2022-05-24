package probando.bookTrading;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import net.sf.clipsrules.jni.*;
import java.util.*;


class Product{
	private int part_number;
	private String name;
	private String category;
	private int price;
	private String paymentMethod;

	public int get_part_number(){
		return part_number;
	}
	public String get_name(){
		return name;
	}
	public String get_category(){
		return category;
	}
	public int get_price(){
		return price;
	}
	public String get_paymentMethod(){
		return paymentMethod;
	}
	

	Product(int part_number, String name, String category, int price){
		this.part_number = part_number;
		this.name = name;
		this.category = category;
		this.price = price;
	};
}

class Catal {
	public ArrayList<Product> list = new ArrayList<>();
	Catal(){}
	public void addProduct( Product p ){
		list.add(p);
	}
	public int prodCount(){
		return list.size();
	}
	public int productExists( String title, String payment ){
		for (int i = 0; i < list.size(); i++) {
			if( list.get(i).get_name() == title ){
				// Product exists. Now check for payment method
				System.out.println("Seller-");


				
			}else{
				return -1;
			}
		}
		return -1;
	}
}
public class BookSellerAgent extends Agent {
	
	// The catalogue of books for sale (maps the title of a book to its price)
	private Hashtable catalogue;
	private Catal myCatalogue;
	// The GUI by means of which the user can add books in the catalogue
	private BookSellerGui myGui;

	// Put agent initializations here
	public Environment clips;

	protected void setup() {
		try {
			clips = new Environment();

			clips.build("(deftemplate product (slot part-number) (multislot name) (slot category) (slot price))");
			clips.build("(defrule my-rule11 (product (name ?n) (price 9.99)) => (printout  t \"Customer name found:\"  ?n crlf ))");
			clips.build("(deffacts products (product (part-number 1234) (name USB Memory) (category storage) (price 9.99)) (product (name Amplifier) (category electronics) (part-number 2341) (price 399.99)) (product (name Speakers) (category electronics) (part-number 23241) (price 19.99)) (product (name iPhone 7) (category smartphone) (part-number 3412) (price 99.99)) (product (name Samsung Edge 7) (category smartphone) (part-number 34412) (price 88.99)) )");
			// Cards
			clips.build("(deftemplate tarjeta (slot type) (slot company) (slot level) )");
			clips.build("(deffacts tarjetas (tarjeta (type mastercard ) (company banamex) (level basic )) (tarjeta (type visa) (company bbva) (level gold)) (tarjeta (type visa) (company liverpool) (level black)) (tarjeta (type visa) (company oxxo) (level basic)) (tarjeta (type mastercard ) (company mastercard) (level black)))");
			
			


			// // String evalStr = "(find-all-facts ((?f product)) TRUE)";
			// // String evalStr = "(defrule my-rule-kbs ?p <- (product (part-number ?x)  ) (test (= ?x 1234)) => (printout  \"Hola:  \" ?p crlf))";
			// // String evalStr = 

			// MultifieldValue pv = (MultifieldValue) clips.eval("(facts)");
			// System.out.println("\t\t\t...");
			// System.out.println("Esto es lo que guardsamos despuesdel; find facs::::::::: " + pv);
			
			// clips.eval("(list-deftemplates)"); 
			// clips.reset();
			// clips.eval("(facts)"); 
			
			

			// clips.eval("(rules)");
			// clips.run();
		} catch (Exception e){}

		
		// Create the catalogue
		catalogue = new Hashtable();
		myCatalogue = new Catal();

		// Create and show the GUI 
		myGui = new BookSellerGui(this);
		myGui.showGui();

		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add the behaviour serving queries from buyer agents
		addBehaviour(new OfferRequestsServer());

		// Add the behaviour serving purchase orders from buyer agents
		addBehaviour(new PurchaseOrdersServer());
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Close the GUI
		myGui.dispose();
		// Printout a dismissal message
		System.out.println("Seller-agent "+getAID().getName()+" terminating.");
	}

	/**
     This is invoked by the GUI when the user adds a new book for sale
	 */
	public void updateCatalogue(final String title, final int price, final String category, final int part_number) {
		//IN THE CLASS parte nombre cat precio
		
		
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				Product p = new Product(part_number, title, category, price);


				myCatalogue.addProduct(p);
				
				catalogue.put(title, new Integer(price));

				// clips.eval("(assert (product (part-number "+ part_number +") (name "+ title +") (category "+ category +") (price "+ price +")))");
				// clips.build("(deffacts products (product (part-number "+part_number+") (name "+title+") (category "+category+") (price "+price+"))  )");
				
				System.out.println(title+" inserted into catalogue. Price = "+price+" category: "+category);
				System.out.println(" My catalogue: "+myCatalogue.prodCount());
				// Re build products def facts
				String clipsString = "(deffacts products";

				for (int i = 0; i < myCatalogue.list.size() ; i++) {
					String temp_p = " (product (part-number "+ myCatalogue.list.get(i).get_part_number()+") (name "+myCatalogue.list.get(i).get_name()+") (category "+myCatalogue.list.get(i).get_category()+") (price "+myCatalogue.list.get(i).get_price()+"))";
					clipsString += temp_p;
				}

				clipsString +=")";

				

				clips.build(clipsString);
				clips.reset();
				clips.eval("(facts)"); 
			}
		} );
	}

	/**
	   Inner class OfferRequestsServer.
	   This is the behaviour used by Book-seller agents to serve incoming requests 
	   for offer from buyer agents.
	   If the requested book is in the local catalogue the seller agent replies 
	   with a PROPOSE message specifying the price. Otherwise a REFUSE message is
	   sent back.
	 */
	private class OfferRequestsServer extends CyclicBehaviour {
		public void action() {
			System.out.println("\t--- --- --- ---");
		
		
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// CFP Message received. Process it
				String messageContent = msg.getContent();

				String[] messageContent_array = messageContent.split("|");

				String title;
				title  = messageContent_array[0];
				String payment;
				payment = messageContent_array[1];

				ACLMessage reply = msg.createReply();

				// Integer price = (Integer) catalogue.get(title);
				Integer price = myCatalogue.productExists(title, payment);
				
				// Integer price = (Integer) myCatalogue.list
				if (price != -1) {
					// The requested book is available for sale. Reply with the price
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				}
				else {
					// The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}  // End of inner class OfferRequestsServer

	/**
	   Inner class PurchaseOrdersServer.
	   This is the behaviour used by Book-seller agents to serve incoming 
	   offer acceptances (i.e. purchase orders) from buyer agents.
	   The seller agent removes the purchased book from its catalogue 
	   and replies with an INFORM message to notify the buyer that the
	   purchase has been sucesfully completed.
	 */
	private class PurchaseOrdersServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				Integer price = (Integer) catalogue.remove(title);
				if (price != null) {
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(title+" sold to agent "+msg.getSender().getName());
				}
				else {
					// The requested book has been sold to another buyer in the meanwhile .
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}  // End of inner class OfferRequestsServer
}