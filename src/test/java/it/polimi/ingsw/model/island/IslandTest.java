package it.polimi.ingsw.model.island;

import it.polimi.ingsw.model.assistant.AssistantCard;
import it.polimi.ingsw.model.character.CardEffect;
import it.polimi.ingsw.model.character.CharacterCard;
import it.polimi.ingsw.model.cloud.CloudCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameMode;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerNumber;
import it.polimi.ingsw.model.player.Team;
import it.polimi.ingsw.model.school.Prof;
import it.polimi.ingsw.model.school.School;
import it.polimi.ingsw.model.school.TColor;
import it.polimi.ingsw.model.school.Tower;
import it.polimi.ingsw.model.student.SColor;
import it.polimi.ingsw.model.student.Student;
import it.polimi.ingsw.model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.out;
import static org.junit.Assert.*;

public class IslandTest {
   private Table table;
   private ArrayList<Player> players;

   public static final  String ANSI_RESET = "\u001B[0m";
   public static final  String ANSI_GREEN = "\u001B[32m";
   public static final  String ANSI_YELLOW = "\u001B[33m";
   public static final  String ANSI_RED = "\u001B[31m";
   public static final  String ANSI_PINK = "\u001B[35m";
   public static final  String ANSI_BLUE = "\u001B[34m";
   public static final  String ANSI_GREY = "\u001B[37m";
   public static final  String ANSI_BLACK = "\u001B[30m";

   @BeforeEach
   public void setup(){
      table = new Table();
      players = new ArrayList<>();
      table.addFinalStudents();
      table.generateIslandCards();
      table.generateMotherEarth();

      players.add(new Player(TColor.WHITE, PlayerNumber.PLAYER1));
      players.add(new Player(TColor.BLACK, PlayerNumber.PLAYER2));
   }

   private void ShowTable(Table table){
      int i=1;

      out.print("\n**** TABLE ****");
      out.print("\nIsland Id  | MotherEarth |   Tower (n)   | Students");
      for(IslandCard islandCard : table.getListOfIsland()) {
         if (islandCard.towerIsOnIsland()) {
            out.print("\n" + islandCard.getIdIsland() + "          | " + islandCard.getMotherEarthOnIsland() + "       |    " + getTowerAnsiColor(islandCard.getTowerOnIsland()) + "T" +ANSI_RESET+ "(" + islandCard.getMergedIsland() + ")     | ");
            for (Student student: islandCard.getStudentOnIsland())
               out.print(getStudentAnsiColor(student) + student.getIdStudent() + " " + ANSI_RESET);
         } else {
            out.print("\n" + islandCard.getIdIsland() + "          | " + islandCard.getMotherEarthOnIsland() + "       | " + " No Tower" +"(" + (islandCard.getMergedIsland()-1) + ")" + " | ");
            for (Student student: islandCard.getStudentOnIsland())
               out.print(getStudentAnsiColor(student) + student.getIdStudent() + " " + ANSI_RESET);
         }
      }
      out.println("\n----------------------------------------------");
      out.print("\n**** CLOUDS ****");
      for(CloudCard c : table.getCloudNumber()){
         out.print("\nCloud "+ table.getCloudNumber().get(i-1).getIdCloud() + ") ");
         if (table.getCloudNumber().get(i-1).getStudentOnCloud().size()>0) {
            out.print("Id Students: " );
            for (Student s : table.getCloudNumber().get(i - 1).getStudentOnCloud()) {
               out.print(getStudentAnsiColor(s) + s.getIdStudent() + ANSI_RESET + " | ");

            }
         }else
            out.print("Empty");
         i++;
      }
      out.println("\n----------------------------------------------");
   }

   public void showPersonalSchool(School school, String nickname, AssistantCard trash) {

      out.print("\n****School of "+ nickname + "**** ");
      out.print("\nEntry: ");
      for(Student s : school.getEntry()){
         out.print(getStudentAnsiColor(s) + s.getIdStudent() + ANSI_RESET + " | " );
      }
      out.print(ANSI_GREEN +"\n\nProf: "+ ANSI_RESET +school.getProfInHall(SColor.GREEN)+ ANSI_GREEN + " Green Table: " + ANSI_RESET);
      for(Student s : school.getGTable()){
         out.print(getStudentAnsiColor(s) + s.getIdStudent() + ANSI_RESET + " | ");
      }

      out.print(ANSI_RED +"\nProf: "+ ANSI_RESET +school.getProfInHall(SColor.RED)+ ANSI_RED + " Red Table: " + ANSI_RESET);
      for(Student s : school.getRTable()){
         out.print(getStudentAnsiColor(s) + s.getIdStudent() + ANSI_RESET + " | ");
      }

      out.print(ANSI_YELLOW +"\nProf: "+ ANSI_RESET +school.getProfInHall(SColor.YELLOW)+ ANSI_YELLOW + " Yellow Table: " + ANSI_RESET);
      for(Student s : school.getYTable()){
         out.print(getStudentAnsiColor(s) + s.getIdStudent() + ANSI_RESET + " | ");
      }

      out.print(ANSI_PINK +"\nProf: "+ ANSI_RESET +school.getProfInHall(SColor.PINK)+ ANSI_PINK + " Pink Table: " + ANSI_RESET);
      for(Student s : school.getPTable()){
         out.print(getStudentAnsiColor(s) + s.getIdStudent() + ANSI_RESET + " | ");
      }

      out.print(ANSI_BLUE +"\nProf: "+ ANSI_RESET +school.getProfInHall(SColor.BLUE)+ ANSI_BLUE + " Blue Table: " + ANSI_RESET);
      for(Student s : school.getBTable()){
         out.print(getStudentAnsiColor(s) + s.getIdStudent() + ANSI_RESET + " | ");
      }

      out.print("\n\nTowers: ");
      for(Tower t : school.getTowers()){
         out.print(getTowerAnsiColor(t) + "T" + ANSI_RESET + " | ");
      }
      out.print("(" + school.getTowers().size() + " towers remained)");

      out.print("\n\nTrash Card: ");
      if (trash!=null)
         out.print(trash.getAssistantName() +" (TurnValue: "+ trash.getTurnValue() + ", StepME: " + trash.getStepMotherEarth()+")");

      out.println("\n----------------------------------------------");
   }

   private String getStudentAnsiColor(Student student) {
      switch (student.getSColor()) {
         case GREEN:
            return ANSI_GREEN;
         case YELLOW:
            return ANSI_YELLOW;
         case RED:
            return ANSI_RED;
         case PINK:
            return ANSI_PINK;
         case BLUE:
            return ANSI_BLUE;
         default:
            return ANSI_RESET;
      }
   }


   private String getTowerAnsiColor(Tower tower) {
      switch (tower.getTColour()) {
         case BLACK:
            return ANSI_BLACK;
         case GREY:
            return ANSI_GREY;
         default:
            return ANSI_RESET;
      }
   }

      @Test
   public void IslandCard(){
      for(int count = 0; count < 12; count++){
         IslandCard island = new IslandCard(count);

         assertNotNull(island.getIdIsland());
         assertNotNull(island.getStudentOnIsland());
         assertNotNull(island.getMergedIsland());
         assertNull(island.getTowerOnIsland());

         assertEquals(count, island.getIdIsland());
         assertEquals(false, island.towerIsOnIsland());
         assertEquals(1, island.getMergedIsland());
         assertEquals(false, island.isXCardOnIsland());
      }

      //cambio indice
      for(int i=0; i<10; i++){
         IslandCard island = new IslandCard(i);
         assertEquals(i, island.getIdIsland());
         island.setIdIsland(island.getIdIsland()+1);
         assertNotNull(island.getIdIsland());
         assertEquals(i+1, island.getIdIsland());
      }

   }

   @Test
   public void MergedIsland(){
      int i,count = 0;

      for(count = 0; count < 12; count++){
         IslandCard island = new IslandCard(count);
         for(i=0; i<8; i++){
            island.setMergedIsland(i);
            assertNotNull(island.getMergedIsland());
            assertEquals(i, island.getMergedIsland());
         }
      }
   }

   @Test
   public void MotherEarthOnIsland(){
      int count = 0; //ripete su tutte le isole

      for(count = 0; count < 12; count++) {
         IslandCard island = new IslandCard(count);
         assertNotNull(island.getMergedIsland());
         assertEquals(false, island.getMotherEarthOnIsland());

         island.setMotherEarthOnIsland(true);
         assertNotNull(island.getMergedIsland());
         assertEquals(true, island.getMotherEarthOnIsland());
      }
   }


   @ParameterizedTest
   @EnumSource(TColor.class)
   public void TowerOnIsland(TColor color){
      int count = 0; //ripete su tutte le isole
      int i = 0; //indice delle torri in TowerZone
      Player player = new Player(color, PlayerNumber.PLAYER1);
      GameMode gm = GameMode.TWOPLAYERS;
      player.generateSchool(table, gm);
      IslandCard island = new IslandCard(count);

      assertNotNull(island.towerIsOnIsland());
      assertEquals(false, island.towerIsOnIsland());
      assertNull(island.getTowerOnIsland());

      for(count = 0; count < 12; count++){
         for(i=0; i<7; i++){
            switch (color){
               case WHITE:
               case BLACK:
               case GREY:
                  Tower tw = new Tower(color);
                  island.setTowerOnIsland(player.getPersonalSchool().getTowers().get(i));
                  island.setTowerIsOnIsland(true);
                  assertNotNull(island.getTowerOnIsland());
                  assertNotNull(island.towerIsOnIsland());
                  assertEquals(tw.getTColour(), island.getTowerOnIsland().getTColour());
                  assertEquals(true, island.towerIsOnIsland());
                  break;
            }
         }
      }
   }


   @Test
   public void XCardOnIsland(){
      int count = 0;
      int countX = 0;

      for(count = 0; count < 12; count++){
         IslandCard island = new IslandCard(count);
         assertNotNull(island.isXCardOnIsland());
         assertEquals(false, island.isXCardOnIsland());

         for(countX = 0; countX < 4; countX++){
            assertEquals(countX, island.getXCardCounter());
            island.setXCardCounter(island.getXCardCounter() + 1);
            island.setXCardOnIsland(true);
            assertNotNull(island.getXCardCounter());
            assertEquals(countX + 1, island.getXCardCounter());
         }
      }
   }

   @RepeatedTest(100)
   public void CalculateInfluenceTest(){
      Random rn = new Random();
      int r;
      Game game= new Game();
      for(int index=0;index<2;index++) { /** For the first two GameModes */
         table.getBag().clear();
         table.getListOfIsland().clear();
         table.generateIslandCards();
         table.generateMotherEarth();
         table.generateBagInit();
         table.extractStudentsInit();
         table.addFinalStudents();
         game.getListOfPlayers().clear();
         game.setGameMode(GameMode.values()[index]);
         for (int i = 0; i < index + 2; i++) {         /** Adds players based on the GameMode */
            game.getListOfPlayers().add(new Player(TColor.values()[i], PlayerNumber.values()[i]));
            game.getListOfPlayers().get(i).generateSchool(table, GameMode.values()[index]);
         }
         for (int i = 0; i < index + 2; i++) {      /** Students are positioned randomly in the different Tables */
            for (int s = 1; s < 6; s++) {
               r = rn.nextInt(4) + 1;
               switch (s) {
                  case 1:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getGTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 2:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getRTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 3:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getYTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 4:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getPTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 5:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getBTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
               }
            }
         }
         for (int i = 0; i < index + 2; i++) {    /** Positions the Professors according to the rules */
            game.getListOfPlayers().get(i).getPersonalSchool().winProf(game.getListOfPlayers(), game.getListOfPlayers().get(i), CardEffect.STANDARDMODE);
         }
         for (int i = 0; i < 12; i++) {  /** Moves random number of students on Islands. */
            r = rn.nextInt(4) + 1;
            for (int n = 0; n < r; n++) {
               table.getListOfIsland().get(i).getStudentOnIsland().add(table.getBag().get(0));
               table.getBag().remove(0);
            }
            ArrayList<Integer> countInfluence = new ArrayList<>();
            for (Player player : game.getListOfPlayers()) {      /** Calculates manually the influence for each player on that particular island. */
               int s = 0;
               for (Prof prof : player.getPersonalSchool().getProfOfPlayer()) {
                  if (prof.getIsInHall()) {
                     for (Student student : table.getListOfIsland().get(i).getStudentOnIsland()) {
                        if (student.getSColor() == prof.getSColor())
                           s++;
                     }
                  }
               }
               countInfluence.add(s);
            }
            table.getListOfIsland().get(i).calculateInfluence(game.getListOfPlayers(), CardEffect.STANDARDMODE, null, GameMode.values()[index]);
            int max = 0, n = 0;
            /** Checks if the influence calculated manually for each player is the same calculated by the function "calculateInfluence"
             *  and finds the maximum between them*/
            for (Player player : game.getListOfPlayers()) {
               assertEquals(countInfluence.get(game.getListOfPlayers().indexOf(player)).intValue(), player.getInfluenceOnIsland());
               if (max < player.getInfluenceOnIsland()) {
                  max = player.getInfluenceOnIsland();
               }
            }
            Player playerWithMaxInfluence = null;
            for (Player player : game.getListOfPlayers()) {    /** Differentiates between the case of victory and tie with n*/
               if (player.getInfluenceOnIsland() == max) {
                  n++;
                  playerWithMaxInfluence = player;
               }
            }
            if (n == 1)   /** Checks if the player selected by the function is the one with the most influence*/
               assertEquals(playerWithMaxInfluence, table.getListOfIsland().get(i).calculateInfluence(game.getListOfPlayers(), CardEffect.STANDARDMODE, null, GameMode.values()[index]));
            else if (n > 1)  /** Checks if in case of a tie the function returns null */
               assertNull(table.getListOfIsland().get(i).calculateInfluence(game.getListOfPlayers(), CardEffect.STANDARDMODE, null, GameMode.values()[index]));
         }
      }
   }




   @RepeatedTest(100)
   public void BuildTowerOnIslandTest(){
      Random rn = new Random();
      int r;
      Game game= new Game();
      for(int index=0;index<2;index++) { /** For the first two GameModes */
         table.getBag().clear();
         table.getListOfIsland().clear();
         table.generateIslandCards();
         table.generateMotherEarth();
         table.generateBagInit();
         table.extractStudentsInit();
         table.addFinalStudents();
         game.getListOfPlayers().clear();
         game.setGameMode(GameMode.values()[index]);
         for (int i = 0; i < index + 2; i++) {         /** Adds players based on the GameMode */
            game.getListOfPlayers().add(new Player(TColor.values()[i], PlayerNumber.values()[i]));
            game.getListOfPlayers().get(i).generateSchool(table, GameMode.values()[index]);
         }
         for (int i = 0; i < index + 2; i++) {      /** Students are positioned randomly in the different Tables */
            for (int s = 1; s < 6; s++) {
               r = rn.nextInt(4) + 1;
               switch (s) {
                  case 1:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getGTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 2:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getRTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 3:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getYTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 4:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getPTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
                  case 5:
                     for (int t = 1; t < r; t++) {
                        game.getListOfPlayers().get(i).getPersonalSchool().getBTable().add(table.getBag().get(0));
                        table.getBag().remove(0);
                     }
                     break;
               }
            }
         }
         for (int i = 0; i < index + 2; i++) {    /** Positions the Professors according to the rules */
            game.getListOfPlayers().get(i).getPersonalSchool().winProf(game.getListOfPlayers(), game.getListOfPlayers().get(i), CardEffect.STANDARDMODE);
         }
         for (int i = 0; i < 12; i++) {  /** Moves random number of students on Islands. */
            r = rn.nextInt(4) + 1;
            for (int n = 0; n < r; n++) {
               table.getListOfIsland().get(i).getStudentOnIsland().add(table.getBag().get(0));
               table.getBag().remove(0);
            }
         }

         ShowTable(table);
         for (int i = 0; i < game.getListOfPlayers().size(); i++)
            showPersonalSchool(game.getListOfPlayers().get(i).getPersonalSchool(), game.getListOfPlayers().get(i).getPlayerNumber().toString(), null);

         table.moveMotherEarth(13 - table.getPosMotherEarth());
         for (IslandCard islandCard : table.getListOfIsland()) {
            if (islandCard.towerIsOnIsland())
               assertEquals(islandCard.calculateInfluence(game.getListOfPlayers(), CardEffect.STANDARDMODE, null, null).getTColor(), islandCard.getTowerOnIsland().getTColour());
            table.moveMotherEarth(1);
         }
      }
   }

   @Test
   public void buildTowerCoopAndChangeTowerColorAndEffects(){
      IslandCard island = new IslandCard(1);
      island.setMotherEarthOnIsland(true);

      island.getStudentOnIsland().add(new Student(131,SColor.GREEN));
      island.getStudentOnIsland().add(new Student(132,SColor.PINK));
      island.getStudentOnIsland().add(new Student(133,SColor.RED));
      island.getStudentOnIsland().add(new Student(134,SColor.YELLOW));
      island.getStudentOnIsland().add(new Student(135,SColor.YELLOW));

      players.clear();
      players.add(new Player(TColor.WHITE,PlayerNumber.PLAYER1));
      players.add(new Player(TColor.WHITE,PlayerNumber.PLAYER2));
      players.add(new Player(TColor.BLACK,PlayerNumber.PLAYER3));
      players.add(new Player(TColor.BLACK,PlayerNumber.PLAYER4));

      Table testTable = new Table();
      testTable.getListOfIsland().add(island);
      testTable.addFinalStudents();

      for(Player player : players)
         player.generateSchool(table, GameMode.COOP);

      players.get(0).getPersonalSchool().getGTable().add(new Student(136,SColor.GREEN));
      players.get(0).getPersonalSchool().getRTable().add(new Student(137,SColor.RED));
      players.get(0).getPersonalSchool().winProf(players, players.get(0),CardEffect.STANDARDMODE);

      players.get(1).getPersonalSchool().getPTable().add(new Student(138,SColor.PINK));
      players.get(1).getPersonalSchool().winProf(players, players.get(1),CardEffect.STANDARDMODE);

      players.get(3).getPersonalSchool().getYTable().add(new Student(139,SColor.YELLOW));
      players.get(3).getPersonalSchool().getBTable().add(new Student(140,SColor.BLUE));
      players.get(3).getPersonalSchool().winProf(players, players.get(3),CardEffect.STANDARDMODE);

      players.get(0).setNickname("Fede");
      players.get(0).setTeamMate("Tri");
      players.get(0).setTeamLeader(true);
      players.get(1).setNickname("Tri");
      players.get(1).setTeamMate("Fede");
      players.get(1).setTeamLeader(false);
      Team team1 = new Team(players.get(0), players.get(1),TColor.WHITE);

      players.get(2).setNickname("Chiara");
      players.get(2).setTeamMate("Boh");
      players.get(2).setTeamLeader(true);
      players.get(3).setNickname("Boh");
      players.get(3).setTeamMate("Chiara");
      players.get(3).setTeamLeader(false);
      Team team2 = new Team(players.get(2), players.get(3), TColor.BLACK);

      ArrayList<Team> teams = new ArrayList<>();
      teams.add(team1);
      teams.add(team2);

      assertEquals(players.get(0),team1.getTeamLeader());
      for(Player p : players)
         showPersonalSchool(p.getPersonalSchool(),p.getNickname(),p.getTrash());

      assertEquals(team1.getTeamLeader(), island.calculateInfluenceCoop(players,CardEffect.STANDARDMODE,players.get(0),teams, GameMode.COOP));

      CharacterCard centaurCard = new CharacterCard(0,CardEffect.CENTAUR,"");
      centaurCard.getCardEffect().setCentaurPlayed(true);

     /**Knight Test */
      CharacterCard knightCard = new CharacterCard(0,CardEffect.KNIGHT,"");
      knightCard.getCardEffect().setKnightPlayed(true);

      island.buildTowerOnIsland(players,CardEffect.STANDARDMODE,players.get(0),GameMode.COOP,teams);
      island.getStudentOnIsland().add(new Student(166,SColor.BLUE));
      assertEquals(null, island.calculateInfluenceCoop(players,centaurCard.getCardEffect(),players.get(0),teams, GameMode.COOP));
      island.getStudentOnIsland().remove(island.getStudentOnIsland().size()-1);
      assertEquals(true, island.towerIsOnIsland());
      assertEquals(TColor.WHITE, island.getTowerOnIsland().getTColour());

      players.remove(1);
      players.remove(1);
      island.getStudentOnIsland().add(new Student(145,SColor.BLUE));
      out.println("KNIGHT TEST");
      for(Player p : players)
         showPersonalSchool(p.getPersonalSchool(),p.getNickname(),p.getTrash());

      for(Student s : island.getStudentOnIsland())
         out.println(getStudentAnsiColor(s)+s.getIdStudent());

      assertEquals(players.get(0), island.calculateInfluence(players, knightCard.getCardEffect(),players.get(0),GameMode.TWOPLAYERS));

      /** Centaur test*/
      centaurCard.getCardEffect().setCentaurPlayed(true);
      assertEquals(players.get(1), island.calculateInfluence(players, centaurCard.getCardEffect(),players.get(0),GameMode.TWOPLAYERS));
      centaurCard.getCardEffect().setCentaurPlayed(true);
      /** ChangeColorTower Test */
      island.buildTowerOnIsland(players,centaurCard.getCardEffect(),players.get(0),GameMode.TWOPLAYERS, null);
      assertEquals(true, island.towerIsOnIsland());
      assertEquals(TColor.BLACK, island.getTowerOnIsland().getTColour());

      /** Herald Test */
      IslandCard islandHerald = new IslandCard(1);
      islandHerald.getStudentOnIsland().add(new Student(131,SColor.GREEN));
      islandHerald.getStudentOnIsland().add(new Student(132,SColor.RED));
      islandHerald.getStudentOnIsland().add(new Student(133,SColor.RED));
      islandHerald.getStudentOnIsland().add(new Student(134,SColor.YELLOW));
      islandHerald.getStudentOnIsland().add(new Student(135,SColor.YELLOW));
      CharacterCard heraldCard = new CharacterCard(0,CardEffect.HERALD, "");

      testTable.getListOfIsland().remove(0);
      testTable.getListOfIsland().add(islandHerald);

      islandHerald.buildTowerOnIsland(players,heraldCard.getCardEffect(),players.get(0),GameMode.TWOPLAYERS,null);
      assertEquals(true, islandHerald.towerIsOnIsland());
      assertEquals(TColor.WHITE, islandHerald.getTowerOnIsland().getTColour());
      assertEquals(false,islandHerald.getMotherEarthOnIsland());


      /** Knight with 0 students test */
      knightCard.getCardEffect().setKnightPlayed(true);
      island.getStudentOnIsland().clear();
      assertEquals(players.get(0),island.calculateInfluence(players, knightCard.getCardEffect(),players.get(0),GameMode.TWOPLAYERS));
   }
}
