import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulation single entry point.
 */
public class Main {
	
	/**
	 * For keep tracking of wins.
	 */
	private static class Win{
		int symbol;
		int size;
		
		/**
		 * Constructor with all parameters.
		 * 
		 * @param symbol Symbol on the screen.
		 * @param size Size of its cluster.
		 */
		public Win(int symbol, int size) {
			super();
			this.symbol = symbol;
			this.size = size;
		}
	}
	
	/**
	 * Pseudo-random number generator.
	 */
	private static final Random PRNG = new Random();
	
	/**
	 * Minimum wining cluster size.
	 */
	private static final int MIN_CLUSTER_SIZE = 3;
	
	/**
	 * Maximum wining cluster size.
	 */
	private static final int MAX_CLUSTER_SIZE = 30;
	
	/**
	 * Empty constant used for back tracking algorithm.
	 */
	private static final int EMPTY = -1;
	
	/**
	 * Current visible symbols on the screen.
	 */
	private static int[][] view = {
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
	};
	
	/**
	 * Current visible screen clusters.
	 */
	private static int[][] clusters = {
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
		{ -1, -1, -1, -1, -1, -1 },
	};
	
	/**
	 * Keep track of the winning information.
	 */
	private static List<Win> winnings = new ArrayList<>();
	
	/**
	 * List of symbols names.
	 */
	private static final String[] symbols = {
		"             ",
		"             ",
		"             ",
		"Single Milk  ",
		"Single White ",
		"Choco Spiral ",
		"Lollipop     ",
		"Heart Stripe ",
		"Choco Heart  ",
		"Choco Round  ",
		"Choco Shell  ",
		"Spiral Stripe",
		"Top Free     ",
		"Bottom Free  ",
		"Top Milk     ",
		"Bottom Milk  ",
		"Top White    ",
		"Bottom White ",
		"             ",
		"             ",
	};
	
	/**
	 * Slot game pay table.
	 */
	private static final int[][] paytable = {
		{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
		{0,	0,	0,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	20,	20,	20,	20,	20,	20,	20,	20,	20,	20,	20,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	30,	30,	30,	30,	30,	30,	30,	30,	30,	30,	30,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	40,	40,	40,	40,	40,	40,	40,	40,	40,	40,	40,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	50,	50,	50,	50,	50,	50,	50,	50,	50,	50,	50,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	60,	60,	60,	50,	60,	60,	60,	60,	60,	60,	60,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	70,	70,	70,	50,	70,	70,	70,	70,	70,	70,	70,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	780,	510,	240,50,	80,	80,	80,	80,	80,	80,	80,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	790,	530,	270,	50,	90,	90,	90,	90,	90,	90,	90,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	800,	550,	300,	50,	100,	100,	100,	100,	100,	100,	100,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	810,	570,	330,	90,	110,	110,	110,	110,	110,	110,	110,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	820,	590,	360,	130,	120,	120,	120,	120,	120,	120,	120,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	830,	610,	390,	170,	130,	130,	130,	130,	130,	130,	130,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	840,	630,	420,	210,	140,	140,	140,	140,	140,	140,	140,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	850,	650,	450,	250,	150,	150,	150,	150,	150,	150,	150,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	860,	670,	480,	290,	150,	160,	160,	160,	160,	160,	160,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	870,	690,	510,	330,	150,	170,	170,	170,	170,	170,	170,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	880,	710,	540,	370,	200,	180,	180,	180,	180,	180,	180,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	890,	730,	570,	410,	250,	190,	190,	190,	190,	190,	190,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	900,	750,	600,	450,	300,	200,	200,	200,	200,	200,	200,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	910,	770,	630,	490,	350,	210,	210,	210,	210,	210,	210,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	920,	790,	660,	530,	400,	270,	220,	220,	220,	220,	220,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	930,	810,	690,	570,	450,	330,	230,	230,	230,	230,	230,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	940,	830,	720,	610,	500,	390,	280,	240,	240,	240,	240,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	950,	850,	750,	650,	550,	450,	350,	250,	260,	250,	250,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	960,	870,	780,	690,	600,	510,	420,	330,	270,	260,	260,	10,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	970,	890,	810,	730,	650,	570,	490,	410,	330,	270,	270,	90,	10,	10,	10,	10,	0,	0},
		{0,	0,	0,	980,	910,	840,	770,	700,	630,	560,	490,	420,	350,	280,	210,	140,	70,	10,	10,	0,	0},
		{0,	0,	0,	990,	930,	870,	810,	750,	690,	630,	570,	510,	450,	390,	330,	270,	210,	150,	90,	0,	0},
		{0,	0,	0,	1000,	950,	900,	850,	800,	750,	700,	650,	600,	550,	500,	450,	400,	350,	300,	250,	0,	0},
	};
	
	/**
	 * Slot game pay table.
	 */
	private static final int[][] blocks = {
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,},
		{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,},
		{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,},
		{0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,},
		{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,},
		{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,},
		{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,},
	};

	/**
	 * Strips with symbols.
	 * 
	 * Free spins, double milk and double white are symbols which should be always in pairs.
	 */
	private static final int[][] reels = {
		{11,8,9,7,8,4,12,13,7,5,9,7,8,7,16,17,7,16,17,8,14,15,5,7,10,16,8,3,14,15,9,9,10,7,8,7,16,17,3,12,13,4,9,11,10,16,17,7,16,17,5,3,8,8,10,16,17,7,6,8,11,7,3,},
		{8,4,10,4,7,12,13,16,17,8,9,7,5,11,14,15,10,12,13,4,10,10,10,4,4,8,10,4,5,6,16,17,3,11,16,17,6,10,3,9,14,15,5,16,17,9,3,16,17,12,13,5,10,6,4,7,5,16,17,14,15,3,5,},
		{10,8,11,9,3,8,4,10,14,15,5,16,17,6,5,3,11,3,7,11,10,14,15,5,3,10,4,16,17,14,15,12,13,10,12,13,4,14,15,5,6,16,17,5,8,9,3,4,7,8,6,3,16,17,3,5,9,3,4,6,4,5,7,},
		{14,15,3,14,15,7,16,17,7,4,16,17,3,4,6,7,12,13,7,16,17,8,16,17,8,3,4,5,8,9,10,4,9,9,5,8,3,9,14,15,3,4,12,13,10,11,9,8,3,4,5,12,13,7,3,4,12,13,5,6,5,8,6,},
		{3,6,5,4,6,11,9,4,6,5,4,16,17,4,7,5,8,6,7,14,15,10,3,6,8,7,12,13,10,3,8,11,16,17,14,15,12,13,3,4,16,17,9,6,16,17,16,17,5,3,7,10,11,10,12,13,10,16,17,5,3,4,9,},	
	};
	
	/**
	 * Total bet in single base game spin.
	 * 
	 * May be it can be related with the number of the clusters.
	 */
	private static int totalBet = 30;
	
	/**
	 * Total amount of won money.
	 */
	private static long totalWonMoney = 0L;
	
	/**
	 * Total amount of lost money.
	 */
	private static long totalLostMoney = 0L;
	
	/**
	 * Total amount of won money in base game.
	 */
	private static long baseGameWonMoney = 0L;
	
	/**
	 * Total amount of won money in free spins.
	 */
	private static long freeGamesWonMoney = 0L;
	/**
	 * Max amount of won money in base game.
	 */
	private static long baseMaxWin = 0L;
	
	/**
	 * Max amount of won money in free spins.
	 */
	private static long freeMaxWin = 0L;
	/**
	 * Total number of base games played.
	 */
	private static long totalNumberOfBaseGames = 0L;
	
	/**
	 * Total number of free spins played.
	 */
	private static long totalNumberOfFreeGames = 0L;
	
	/**
	 * Total number of free spins started.
	 */
	private static long totalNumberOfFreeGamesStarts = 0L;
	
	/**
	 * Hit rate of wins in base game.
	 */
	private static long baseGameHitRate = 0L;
	
	/**
	 * Hit rate of wins in free spins.
	 */
	private static long freeGamesHitRate = 0L;
	
	/**
	 * Free spins flag.
	 */
	private static boolean freeGamesOff = false;
	
	/**
	 * Wild substitution flag.
	 */
	private static boolean wildsSubstitutionOff = false;	
	
	/**
	 * Verbose output flag.
	 */
	private static boolean verboseOutput = false;
	
	/**
	 * Symbols win hit rate in base game.
	 */
	private static long[][] baseGameSymbolWonMoney = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	};
	
	/**
	 * Symbols hit rate in base game.
	 */
	private static long[][] baseGameSymbolsHitRate = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	};

	/**
	 * Single reels spin to fill view with symbols.
	 *
	 * @param reels Reels strips.
	 */
	private static void spin(int[][] reels) {
		for (int i = 0; i < view.length && i < reels.length; i++) {
			int r = PRNG.nextInt(reels[i].length);
			
			for(int j=0; j < view[i].length; j++) {
				int s = (r + j) % reels[i].length;
				view[i][j] = reels[i][s];
			}
		}
	}

	/**
	 * Clear previous clustering information.
	 */
	private static void claerClustersInfo() {
		clusters = new int[][]{
				{ -1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, -1 },
			};	
			
		winnings.clear();
	}

	/**
	 * Locate cluster and calculate its size.
	 * 
	 * @param view Screen with symbols.
	 * @param clusters Matrix with the clusters already found.
	 * @param symbol Symbol used in the cluster.
	 * @param x Column to start searching. 
	 * @param y Row to start searching.
	 * 
	 * @return Size of the cluster found.
	 */
	private static int locate(int[][] view, int[][] clusters, int symbol, int x, int y) {
		/*
		 * If it is outside of the screen do nothing.
		 */
		if(x < 0) {
			return 0;
		}
		
		/*
		 * If it is outside of the screen do nothing.
		 */
		if(x >= view.length) {
			return 0;
		}
		
		/*
		 * If it is outside of the screen do nothing.
		 */
		if(y < 0) {
			return 0;
		}
		
		/*
		 * If it is outside of the screen do nothing.
		 */
		if(y >= view[x].length) {
			return 0;
		}

		/*
		 * If the cell is different than the cluster color do nothing.
		 */
		if(view[x][y] != symbol) {
			return 0;
		}

		/*
		 * If the cell was visited already do nothing.
		 */
		if(clusters[x][y] != EMPTY) {
			return 0;
		}

		/*
		 * New cluster cell found.
		 */
		clusters[x][y] = symbol;
		return 1 + locate(view, clusters, symbol, x-1, y) + locate(view, clusters, symbol, x+1, y) + locate(view, clusters, symbol, x, y-1) + locate(view, clusters, symbol, x, y+1);
	}

	/**
	 * Calculate cluster wins.
	 * 
	 * @param view Slot game screen.
	 * 
	 * @return Current screen win amount.
	 */
	private static int clustersWin(int[][] view) {
		claerClustersInfo();

		for (int i=0; i<view.length; i++) {
			for(int j=0; j<view[i].length; j++) {
				/*
				 * Cluster was already found.
				 */
				if(clusters[i][j] != EMPTY) {
					continue;
				}
				
				/*
				 * Keep track of the winning clusters.
				 */
				int size = locate(view, clusters, view[i][j], i, j);
				if(MIN_CLUSTER_SIZE <= size && size <= MAX_CLUSTER_SIZE) {
					winnings.add(new Win(view[i][j], size));
				}
			}
		}

		/*
		 * Calculate total win of this screen.
		 */
		int result = 0;
		for(Win win : winnings) {
			result += paytable[win.size][win.symbol];
		}
		
		return result;
	}

	/**
	 * Remove all symbols which were part of a wining cluster.
	 * 
	 * @param view Slot game screen.
	 */
	private static void removeWiningClusters(int[][] view) {
	}

	/**
	 * Put symbols down to fill gaps on the screen.
	 * 
	 * @param view Slot game screen.
	 */
	private static void packEmptySpace(int[][] view) {
	}

	/**
	 * Put symbols over the collapsed structures.
	 * 
	 * @param view Slot game screen.
	 */
	private static void refillEmptySpace(int[][] view) {
	}

	/**
	 * Play single base game.
	 */
	private static void singleBaseGame() {
		/*
		 * Spin reels.
		 */
		spin( reels );

		/* 
		 * Setup free games.
		 */
		
		/*
		 * Win accumulated by lines.
		 */
		int win = clustersWin( view );
		
		/*
		 * Add win to the statistics.
		 */
		baseGameWonMoney += win;
		totalWonMoney += win;
		if(baseMaxWin < win) {
			baseMaxWin = win;
		}
		
		/*
		 * Count base game hit rate.
		 */
		if(win > 0) {
			baseGameHitRate++;
		}

		/*
		 * Handle collapse feature.
		 */
		while(win > 0) {
			win = 0;
			removeWiningClusters( view );
			packEmptySpace( view );
			refillEmptySpace( view );
			
			//TODO win = clustersWin( view );
			
			/*
			 * Add win to the statistics.
			 */
			baseGameWonMoney += win;
			totalWonMoney += win;
			if(baseMaxWin < win) {
				baseMaxWin = win;
			}
		}
		
		/*
		 * Count into free spins hit rate.
		 */
		
		/*
		 * Play all free games.
		 */
	}

	/**
	 * Print screen view.
	 */
	private static void printView () {
		int max = view[0].length;
		for (int i=0; i<view.length; i++) {
			if(max < view[i].length) {
				max = view[i].length;
			}
		}
		
		for(int j=0; j<max; j++) {
			for (int i=0; i<view.length && j<view[i].length; i++) {
				if(view[i][j] < 10 && view[i][j]>=0) {
					System.out.print(" ");
				}
				System.out.print(view[i][j] + " ");
			}
			
			System.out.println();
		}
	}

	/**
	 * Print all simulation input data structures.
	 */
	private static void printDataStructures() {
		/*
		 * Pay table printing.
		 */
		System.out.println("Paytable:");
		for(int i=0; i<paytable.length; i++) {
			System.out.print("\t" + i + " of");
		}
		System.out.println();
		for(int j=0; j<paytable[0].length; j++) {
			System.out.print(symbols[j] + "\t");
			for(int i=0; i<paytable.length; i++) {
				System.out.print(paytable[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println("Base Game Reels:");
		for(int i=0; i<reels.length; i++) {
			for(int j=0; j<reels[i].length; j++) {
				if(j % 10 == 0) {
					System.out.println();
				}
				System.out.print("SYM" + reels[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println("Base Game Reels:");
		/* 
		 * Count symbols in reels. 
		 */ {
			int[][] counters = {
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			};
			for(int i=0; i<reels.length; i++) {
				for(int j=0; j<reels[i].length; j++) {
					counters[i][reels[i][j]]++;
				}
			}
			for(int i=0; i<reels.length; i++) {
				System.out.print("\tReel " + (i+1));
			}
			System.out.println();
			for(int j=0; j<counters[0].length; j++) {
				System.out.print("SYM" + j + "\t");
				for(int i=0; i<counters.length; i++) {
					System.out.print(counters[i][j] + "\t");
				}
				System.out.println();
			}
			System.out.println("---------------------------------------------");
			System.out.print("Total:\t");
			long combinations = 1L;
			for(int i=0; i<counters.length; i++) {
				int sum = 0;
				for(int j=0; j<counters[0].length; j++) {
					sum += counters[i][j];
				}
				System.out.print(sum + "\t");
				if(sum != 0) {
					combinations *= sum;
				}
			}
			System.out.println();
			System.out.println("---------------------------------------------");
			System.out.println("Combinations:\t" + combinations);
		}
		System.out.println();
	}

	/**
	 * Print simulation statistics.
	 */
	private static void printStatistics () {
		System.out.println("Won money:\t" + totalWonMoney);
		System.out.println("Lost money:\t" + totalLostMoney);
		System.out.println("Total Number of Games:\t" + totalNumberOfBaseGames);
		System.out.println();
		System.out.println("Total RTP:\t" + ((double)totalWonMoney / (double)totalLostMoney) + "\t\t" + (100.0D * (double)totalWonMoney / (double)totalLostMoney) + "%");
		System.out.println("Base Game RTP:\t" + ((double)baseGameWonMoney / (double)totalLostMoney) + "\t\t" + (100.0D * (double)baseGameWonMoney / (double)totalLostMoney) + "%");
		System.out.println("Free Game RTP:\t" + ((double)freeGamesWonMoney / (double)totalLostMoney) + "\t\t" + (100.0D * (double)freeGamesWonMoney / (double)totalLostMoney) + "%");
		System.out.println();
		System.out.println("Hit Frequency in Base Game:\t" + ((double)baseGameHitRate / (double)totalNumberOfBaseGames) + "\t\t" + (100.0D * (double)baseGameHitRate / (double)totalNumberOfBaseGames) + "%");
		System.out.println("Hit Frequency into Free Game:\t" + ((double)totalNumberOfFreeGamesStarts / (double)totalNumberOfBaseGames) + "\t\t" + (100.0D * (double)totalNumberOfFreeGamesStarts / (double)totalNumberOfBaseGames) + "%");
		System.out.println();
		System.out.println("Max Win in Base Game:\t" + baseMaxWin);
		System.out.println("Max Win in Free Game:\t" + freeMaxWin);
	}

	/**
	 * Print simulation execution command.
	 *
	 * @param args Command line arguments list.
	 */
	private static void printExecuteCommand(String[] args) {
		System.out.println( "Execute command:" );
		System.out.println();
		System.out.print( "java Main " );
		for(int i=0; i<args.length; i++) {
			System.out.print(args[i] + " ");
		}
		System.out.println();
	}

	/**
	 * Print help information.
	 */
	private static void printHelp () {
		System.out.println( "*******************************************************************************" );
		System.out.println( "* Candy Clusters Slot Simulation version 0.0.0                                *" );
		System.out.println( "* Copyrights (C) 2017                                                         *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* developed by                                                                *" );
		System.out.println( "* India                                                                       *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* This program is free software: you can redistribute it and/or modify        *" );
		System.out.println( "* it under the terms of the GNU General Public License as published by        *" );
		System.out.println( "* the Free Software Foundation, either version 3 of the License, or           *" );
		System.out.println( "* (at your option) any later version.                                         *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* This program is distributed in the hope that it will be useful,             *" );
		System.out.println( "* but WITHOUT ANY WARRANTY; without even the implied warranty of              *" );
		System.out.println( "* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               *" );
		System.out.println( "* GNU General Public License for more details.                                *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* You should have received a copy of the GNU General Public License           *" );
		System.out.println( "* along with this program. If not, see <http://www.gnu.org/licenses/>.        *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "*******************************************************************************" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* -h              Help screen.                                                *" );
		System.out.println( "* -help           Help screen.                                                *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* -g<number>      Number of games (default 10 000 000).                       *" );
		System.out.println( "* -p<number>      Progress on each iteration number (default 10 000 000).     *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* -freeoff        Switch off free spins.                                      *" );
		System.out.println( "* -wildsoff       Switch off wilds.                                           *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "* -verify         Print input data structures.                                *" );
		System.out.println( "*                                                                             *" );
		System.out.println( "*******************************************************************************" );
	}
	
	/**
	 * Method single entry point.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		printExecuteCommand(args);
		System.out.println();
		
		long numberOfSimulations = 10000000L;
		long progressPrintOnIteration = 10000000L;
		
		/*
		 * Parse command line arguments.
		 */
		for(int a=0; a<args.length; a++) {
			if(args.length > 0 && args[a].contains("-g")) {
				String parameter = args[a].substring(2);
				
				if(parameter.contains("k")) {
					parameter = parameter.substring(0, parameter.length()-1);
					parameter += "000";
				}
				
				if(parameter.contains("m")) {
					parameter = parameter.substring(0, parameter.length()-1);
					parameter += "000000";
				}
				
				try {
					numberOfSimulations = Integer.valueOf( parameter );
				} catch(Exception exception) {
				}
			}
			
			if(args.length > 0 && args[a].contains("-p")) {
				String parameter = args[a].substring(2);
				
				if(parameter.contains("k")) {
					parameter = parameter.substring(0, parameter.length()-1);
					parameter += "000";
				}
				
				if(parameter.contains("m")) {
					parameter = parameter.substring(0, parameter.length()-1);
					parameter += "000000";
				}
				
				try {
					progressPrintOnIteration = Integer.valueOf( parameter );
					verboseOutput = true;
				} catch(Exception exception) {
				}
			}

			if(args.length > 0 && args[a].contains("-freeoff")) {
				freeGamesOff = true;
			}
			
			if(args.length > 0 && args[a].contains("-wildsoff")) {
				wildsSubstitutionOff = true;
			}
			
			if(args.length > 0 && args[a].contains("-verify")) {
				printDataStructures();
				System.exit(0);
			}
			
			if(args.length > 0 && args[a].contains("-help")) {
				printHelp();
				System.out.println();
				System.exit(0);
			}
			
			if(args.length > 0 && args[a].contains("-h")) {
				printHelp();
				System.out.println();
				System.exit(0);
			}
		}
		
		/*
		 * Simulation main loop.
		 */
		for (long g = 0L; g < numberOfSimulations; g++) {
			if(verboseOutput == true && g==0) {
				System.out.println("Games\tRTP\tRTP(Base)\tRTP(Free)");
			}

			/*
			 * Print progress report.
			 */
			if(verboseOutput == true && g%progressPrintOnIteration == 0) {
				try {
					System.out.print(g);
					System.out.print("\t");
					System.out.print(String.format("  %12.6f", ((double) totalWonMoney / (double) totalLostMoney)));
					System.out.print("\t");
					System.out.print(String.format("  %12.6f", ((double) baseGameWonMoney / (double) totalLostMoney)));
					System.out.print("\t");
					System.out.print(String.format("  %12.6f", ((double) freeGamesWonMoney / (double) totalLostMoney)));
				} catch( Exception exception) {
				}
				System.out.println();
			}
			
			totalNumberOfBaseGames++;
			totalLostMoney += totalBet;
			singleBaseGame();
		}
		
		System.out.println("********************************************************************************");
		printStatistics();
		System.out.println("********************************************************************************");
	}

}
