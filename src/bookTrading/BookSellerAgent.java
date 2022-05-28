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

class Rule {

	private String p_name;
	private String card_name;
	private String benefitsString;

	Rule(String p_name, String card_name, String benefitsString) {
		this.p_name = p_name;
		this.card_name = card_name;
		this.benefitsString = benefitsString;
	}

	public String get_p_name() {
		return p_name;
	}

	public String get_card_name() {
		return card_name;
	}

	public String get_benefitsString() {
		return benefitsString;
	}
}

class Product {
	private int part_number;
	private String name;
	private String category;
	private int price;
	private String paymentMethod;
	private Rule rule;

	public int get_part_number() {
		return part_number;
	}

	public String get_name() {
		return name;
	}

	public String get_category() {
		return category;
	}

	public int get_price() {
		return price;
	}

	public String get_paymentMethod() {
		return paymentMethod;
	}

	Product(int part_number, String name, String category, int price) {
		this.part_number = part_number;
		this.name = name;
		this.category = category;
		this.price = price;
	};

	public void setRule(Rule r) {
		this.rule = r;
	}
}

class Catal {
	public ArrayList<Product> list = new ArrayList<>();
	public ArrayList<Rule> allRules = new ArrayList<>();

	Catal() {
	}

	public void addProduct(Product p) {
		list.add(p);
	}

	// Adds a rule to allRules property
	public void addRule(Rule r) {
		this.allRules.add(r);
	}

	public int prodCount() {
		return list.size();
	}

	public int productExists(String title, String payment) {
		// System.out.println("tITLE from buyer:" + title);
		int price = 69;
		for (int i = 0; i < list.size(); i++) {
			System.out.println("\t\t Comparing title, get_name: " + title + " , " + list.get(i).get_name());
			// if( list.get(i).get_name().trim() == title.trim() ){
			if (Objects.equals(list.get(i).get_name(), title)) {
				// Product exists. Now check for payment method
				price = list.get(i).get_price();
				System.out.println("\t\t\tResuls=Same title. \tPrice: " + price);
				return price;

			} else {
				System.out.println("\t\t\t Titles didn't match. Price was " + price);
			}
		}
		return -1;
	}

	public String removeByTitle(String title, String payment) {

		int price;
		String benefitsString = " ** No especial benefits in this purchace ** ";
		// Check if a there is a rule with the same product and paymeny
		System.out.println(" All Rules Size: " + this.allRules.size());
		System.out.println(" Looking for rules with: " + title + payment + " comparing");
		for (int i = 0; i < this.allRules.size(); i++) {
			Rule temp_rule = this.allRules.get(i);
			System.out.println(" \t\t\tRULE_GET_NAME\tRULE_GET_CARD_NAME");
			System.out.println("\t\t\t" + temp_rule.get_p_name() + "-\t-" + temp_rule.get_card_name());
			if (Objects.equals(title, temp_rule.get_p_name()) && Objects.equals(payment, temp_rule.get_card_name())) {
				// It means there is a rule with the same product name and card name.
				System.out.println(" ----------------- There is a rule with the same product and payment method");
				benefitsString = temp_rule.get_benefitsString();
			} else {
				// System.out.println(" ----------------- No no");
			}
		}
		// find the product going throught he list and comparing names
		for (int i = 0; i < list.size(); i++) {
			if (Objects.equals(list.get(i).get_name(), title)) {
				// Product exists. Now check for payment method
				price = this.list.get(i).get_price();
				this.list.remove(i);
				return String.valueOf(price) + "-" + benefitsString;
			}
		}
		return null;
	}
}

public class BookSellerAgent extends Agent {

	private BookBuyerAgent myBuyer;

	// The catalogue of books for sale (maps the title of a book to its price)
	private Hashtable catalogue;
	private Catal myCatalogue;
	// The GUI by means of which the user can add books in the catalogue
	private BookSellerGui myGui;

	// Put agent initializations here
	public Environment clips;

	protected void setup() {
		myBuyer = new BookBuyerAgent();
		// Create the catalogue
		catalogue = new Hashtable();
		myCatalogue = new Catal();
		try {
			clips = new Environment();

			clips.build("(deftemplate product (slot part-number) (multislot name) (slot category) (slot price) )");
			// clips.build("(defrule my-rule11 (product (name ?n) (price 9.99)) => (printout
			// t \"Customer name found:\" ?n crlf ))");
			clips.build(
					"(deffacts products (product (part-number 1234) (name USB Memory) (category storage) (price 9.99)) (product (name Amplifier) (category electronics) (part-number 2341) (price 399.99)) (product (name Speakers) (category electronics) (part-number 23241) (price 19.99)) (product (name iPhone 7) (category smartphone) (part-number 3412) (price 99.99)) (product (name Samsung Edge 7) (category smartphone) (part-number 34412) (price 88.99)) )");
			// Cards
			clips.build("(deftemplate tarjeta (slot type) (slot name) (slot level) )");
			clips.build(
					"(deffacts tarjetas (tarjeta (type mastercard ) (name banamex) (level basic )) (tarjeta (type visa) (name bbva) (level gold)) (tarjeta (type visa) (name liverpool) (level black)) (tarjeta (type visa) (name oxxo) (level basic)) (tarjeta (type mastercard ) (name mastercard) (level black)))");
			// Rule 1
			clips.build(
					"(defrule regla1 (product (name samsung) (category smartphone)) (tarjeta (name bbva)) => (printout t \"En la compra, de un smartphone, ofrecemos una funda y mica con el 15% de descuento\" crlf))");
			Rule rule1 = new Rule("samsung", "bbva",
					"En la compra de un smartphone 'samsung' usando bbva ofrecemos una funda y mica con el 15% de descuento");
			myCatalogue.addRule(rule1);
			// Rule 2
			clips.build(
					"(defrule regla2(product (name iphone))(tarjeta (name banamex)) =>(printout t \"En la compra de un iPhone 13, con targeta Banamex, ofrecemos 24 meses sin intereses\" crlf))");
			Rule rule2 = new Rule("iphone", "banamex",
					"En la compra de un iPhone 13, con targeta Banamex, ofrecemos 24 meses sin intereses");
			myCatalogue.addRule(rule2);
			// Rule 3
			clips.build(
					"(defrule regla3 (product (name hp) (category computadora)) (tarjeta (name oxxo)) => (printout t \"En la compra de una HP, con tarjeta oxxo , ofrece 12 meses sin intereses\" crlf))");
			Rule rule3 = new Rule("hp", "oxxo",
					"En la compra de una HP, con tarjeta oxxo , ofrece 12 meses sin intereses");
			myCatalogue.addRule(rule3);
			// Rule 4
			clips.build(
					"(defrule regla4 (product (name mac)) (tarjeta (name mastercard)) => (printout t \"En la compra de una mac, por cada 1000 pesos de compra se entregara 100 pesos en vales\" crlf))");
			Rule rule4 = new Rule("mac", "mastercard",
					"En la compra de una mac, por cada 1000 pesos de compra se entregara 100 pesos en vales");
			myCatalogue.addRule(rule4);

		} catch (Exception e) {
		}

		// Create the catalogue
		catalogue = new Hashtable();

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
		} catch (FIPAException fe) {
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
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Close the GUI
		myGui.dispose();
		// Printout a dismissal message
		System.out.println("Seller-agent " + getAID().getName() + " terminating.");
	}

	/**
	 * This is invoked by the GUI when the user adds a new book for sale
	 */
	public void updateCatalogue(final String title, final int price, final String category, final int part_number) {
		// IN THE CLASS parte nombre cat precio
		System.out.println("@ ADDING PRODUCT");
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				Product p = new Product(part_number, title, category, price);

				myCatalogue.addProduct(p);

				catalogue.put(title, new Integer(price));

				// clips.eval("(assert (product (part-number "+ part_number +") (name "+ title
				// +") (category "+ category +") (price "+ price +")))");
				// clips.build("(deffacts products (product (part-number "+part_number+") (name
				// "+title+") (category "+category+") (price "+price+")) )");

				System.out.println("|\n|\tProduct: " + title + " inserted into catalogue.\n|\t\t Price = " + price
						+ "\n|\tCategory: " + category + "\n|");

				System.out.println("My catalogue has " + myCatalogue.prodCount()+" products");
				// Re build products def facts
				String clipsString = "(deffacts products";

				for (int i = 0; i < myCatalogue.list.size(); i++) {
					String temp_p = " (product (part-number " + myCatalogue.list.get(i).get_part_number() + ") (name "
							+ myCatalogue.list.get(i).get_name() + ") (category "
							+ myCatalogue.list.get(i).get_category() + ") (price " + myCatalogue.list.get(i).get_price()
							+ "))";
					clipsString += temp_p;
				}

				clipsString += ")";

				try {
					clips.build(clipsString);
					clips.reset();
					System.out.println("\nProducts and Cards available (CLIPS FACTS): ");
					clips.eval("(facts)");
					System.out.println();
					System.out.println("Offers and discounts (CLIPS RULES): ");
					clips.eval("(run)");

				} catch (Exception e) {
				}
				System.out.println("\nEnd. Product added.");

			}
		});
	}

	/**
	 * Inner class OfferRequestsServer.
	 * This is the behaviour used by Book-seller agents to serve incoming requests
	 * for offer from buyer agents.
	 * If the requested book is in the local catalogue the seller agent replies
	 * with a PROPOSE message specifying the price. Otherwise a REFUSE message is
	 * sent back.
	 */
	private class OfferRequestsServer extends CyclicBehaviour {
		public void action() {
			System.out.println("--- --- Offer request server --- ---");

			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// CFP Message received. Process it
				String messageContent = msg.getContent().toString();
				System.out.println("Raw message content : " + messageContent);

				String[] splitted = messageContent.split("-");
				System.out.println();
				String title = splitted[0]; // 123
				String payment = splitted[1]; // 654321
				System.out.println("\tBuyer asking for: " + title);
				System.out.println("\tWilling to pay with: " + payment);
				System.out.println();

				ACLMessage reply = msg.createReply();

				// Integer price = (Integer) catalogue.get(title);
				Integer price = myCatalogue.productExists(title, payment);
				System.out.println("\t Found in catalogue? -> Price: " + price);

				// System.out.println("--- End of Offer request ---" );
				// Integer price = (Integer) myCatalogue.list
				if (price != -1) {
					// The requested book is available for sale. Reply with the price
					// try {
					// clips.eval("(run)");
					// } catch (Exception e) {
					// }
					System.out.println("\tYes. Replying to buyer. Product exist. Price:" + price);
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				} else {
					// The requested book is NOT available for sale.
					System.out.println("\tNo. Replying with failure (REFUSE)");
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			} else {
				// System.out.println("Empty offer request");
				block();
			}
		}
	} // End of inner class OfferRequestsServer

	/**
	 * Inner class PurchaseOrdersServer.
	 * This is the behaviour used by Book-seller agents to serve incoming
	 * offer acceptances (i.e. purchase orders) from buyer agents.
	 * The seller agent removes the purchased book from its catalogue
	 * and replies with an INFORM message to notify the buyer that the
	 * purchase has been sucesfully completed.
	 */
	private class PurchaseOrdersServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				System.out.println(" ___ ___ ___Purchace Order received___ ___ ___");
				// ACCEPT_PROPOSAL Message received. Process it
				String messageContent = msg.getContent();
				System.out.println("Raw Message Content in PurchOrder: " + messageContent);
				ACLMessage reply = msg.createReply();

				String[] splitted = messageContent.split("-");
				System.out.println();
				String title = splitted[0]; // 123
				String payment = splitted[1]; // 654321

				System.out.println("\tBuyer ordering to purchace: " + title);
				System.out.println("\tPaying with: " + payment);
				System.out.println();

				// Remove from myCatalogue
				String priceAndBenefits = myCatalogue.removeByTitle(title, payment);
				if (priceAndBenefits != null) {
					// We got a response. Lets split the response top get price and benefits
					String[] splittedPriceAndBenefits = priceAndBenefits.split("-", 2); // 50-** No especial offer **
					String price = splittedPriceAndBenefits[0]; // 50
					String benefits = splittedPriceAndBenefits[1]; // "** No especial offer **" OR "50% discount"

					reply.setPerformative(ACLMessage.INFORM);
					System.out.println("\tDONE!" + title + " sold with benefits:" + benefits + " to agent "
							+ msg.getSender().getName());
				} else {
					// The requested book has been sold to another buyer in the meanwhile .
					System.out.println("Failed to find product");
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			} else {
				// System.out.println("Order blocked.");
				block();
			}
		}
	} // End of inner class OfferRequestsServer
}