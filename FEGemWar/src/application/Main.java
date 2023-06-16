package application;
	
import java.io.FileInputStream;
import java.util.HashMap;

import data_structures.List;
import inventory.misc.UsableItem;
import inventory.weapon.Weapon;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import manager.FEGemWarManager;
import manager.combat.CombatManager;
import map.Chapter;
import map.SeizeObjective;
import map.Tile;
import map.Tile.TileType;
import story.DialogueEvent;
import story.MapEvent;
import story.PickUnitsEvent;
import story.ReinforcementsEvent;
import story.StoryEvent;
import unit.Unit;
import unit.Unit.AIType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			DialogueEvent de = new DialogueEvent(0, "dialogue/opening_dialogue");
			performDialogueEvent(primaryStage, de, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void playNextPartOfSequence(Stage primaryStage, Chapter currentChapter) {
		if (FEGemWarManager.chapterPartIdx >= currentChapter.getAmountInSequence()) {
			FEGemWarManager.chapterIdx++;
			FEGemWarManager.chapterPartIdx = 0;
			currentChapter = FEGemWarManager.loadChapter(FEGemWarManager.chapterIdx);
		}
		StoryEvent event = currentChapter.getEvent(FEGemWarManager.chapterPartIdx);
		FEGemWarManager.chapterPartIdx++;
		if (event instanceof DialogueEvent) {
			performDialogueEvent(primaryStage, (DialogueEvent) event, currentChapter);
		} else if (event instanceof PickUnitsEvent) {
			//TODO
		} else if (event == null) {
			performMapEvent(primaryStage, currentChapter);
		}
	}
	
	public static void performDialogueEvent(Stage primaryStage, DialogueEvent dialog, Chapter chpt) {
		Group group = new Group();
		Scene scene = new Scene(group, 700, 700);
		
		Group background = new Group();
		Group nearLeft = new Group();
		Group nearRight = new Group();
		Group farLeft = new Group();
		Group farRight = new Group();
		Group center = new Group();
		ImageView back = new ImageView();
		ImageView nlImage = new ImageView();
		ImageView nrImage = new ImageView();
		ImageView flImage = new ImageView();
		ImageView frImage = new ImageView();
		ImageView middle = new ImageView();
		background.getChildren().add(back);
		farLeft.getChildren().add(flImage);
		nearLeft.getChildren().add(nlImage);
		farRight.getChildren().add(frImage);
		nearRight.getChildren().add(nrImage);
		center.getChildren().add(middle);

		nearLeft.setTranslateX(400);
		nearLeft.setTranslateY(300);
		nearRight.setTranslateX(150);
		nearRight.setTranslateY(300);
		farLeft.setTranslateY(300);
		farRight.setTranslateX(550);
		farRight.setTranslateY(300);
		center.setTranslateX(275);
		center.setTranslateY(300);
		
		ImageView[] stageParts = new ImageView[] {back, nlImage, nrImage, flImage, frImage, middle};
		
		Group nameBox = new Group();
		Rectangle nameRect = new Rectangle(200, 50);
		nameRect.setFill(Color.ALICEBLUE);
		nameBox.getChildren().add(nameRect);
		Label nameLabel = new Label();
		nameLabel.setFont(new Font(20));
		nameBox.setTranslateY(500);
		nameBox.getChildren().add(nameLabel);
		
		Group textBox = new Group();
		Rectangle textRect = new Rectangle(700, 200);
		textRect.setFill(Color.ALICEBLUE);
		textBox.getChildren().add(textRect);
		Label textLabel = new Label();
		textLabel.setMaxWidth(700);
		textLabel.setWrapText(true);
		textLabel.setFont(new Font(30));;
		textLabel.setTranslateY(50);
		textBox.setTranslateY(500);
		textBox.getChildren().add(textLabel);
		
		try {
			dialog.readDialogue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Button next = new Button("NEXT>>");
		EventHandler<MouseEvent> nextLine = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				List<String> part = dialog.nextDialogueComponent();
				if (part == null) {
					if (chpt == null) {
						performIntroSequence(primaryStage);
					} else {
						playNextPartOfSequence(primaryStage, chpt);
					}
				} else {
					for (int q = 0; q < part.size() - 2; q++) {
						try {
							int pic = Integer.parseInt(part.get(q).substring(0, 1));
							String file = part.get(q).substring(2);
							if (file.equals("clear")) {
								stageParts[pic].setImage(null);
								continue;
							}
							file = "scene_images/" + file;
							if (pic == 0) {
								Image image = new Image(new FileInputStream(file), 700, 500, false, false);
								stageParts[pic].setImage(image);
							} else {
								Image image = new Image(new FileInputStream(file), 150, 200, false, false);
								stageParts[pic].setImage(image);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					String name = part.get(part.size() - 2);
					String speech = part.get(part.size() - 1);
					if (name.equals("_")) {
						name = "";
					}
					if (speech.equals("_")) {
						speech = "";
					}
					nameLabel.setText(name);
					textLabel.setText(speech);
				}
			}
		};
		EventHandler<KeyEvent> pressNextLine = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCharacter().equals("Z")
						|| arg0.getCharacter().equals("z")) {
					nextLine.handle(null);
				}
			}
		};
		next.addEventFilter(MouseEvent.MOUSE_CLICKED, nextLine);
		next.addEventHandler(KeyEvent.KEY_TYPED, pressNextLine);
		
		next.setTranslateX(600);
		next.setTranslateY(650);
		
		
		List<String> part = dialog.nextDialogueComponent();
		for (int q = 0; q < part.size() - 2; q++) {
			try {
				int pic = Integer.parseInt(part.get(q).substring(0, 1));
				String file = part.get(q).substring(2);
				if (file.equals("clear")) {
					stageParts[pic].setImage(null);
					continue;
				}
				file = "scene_images/" + file;
				if (pic == 0) {
					Image image = new Image(new FileInputStream(file), 700, 500, false, false);
					stageParts[pic].setImage(image);
				} else {
					Image image = new Image(new FileInputStream(file), 150, 200, false, false);
					stageParts[pic].setImage(image);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		String name = part.get(part.size() - 2);
		String speech = part.get(part.size() - 1);
		if (name.equals("_")) {
			name = "";
		}
		if (speech.equals("_")) {
			speech = "";
		}
		nameLabel.setText(name);
		textLabel.setText(speech);

		
		group.getChildren().add(background);
		group.getChildren().add(farLeft);
		group.getChildren().add(nearLeft);
		group.getChildren().add(farRight);
		group.getChildren().add(nearRight);
		group.getChildren().add(center);
		group.getChildren().add(textBox);
		group.getChildren().add(nameBox);
		group.getChildren().add(next);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void performInMapDialogueEvent(Stage primaryStage,
			GridPane map, Group info, Group click, ScrollPane pane,
			DialogueEvent dialog, Chapter chpt,
			Unit initiator, Unit listener, Tile listenerTile) {
		
		map.getChildren().clear();
		Tile[][] tileMap = chpt.getMap().getMap();
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				Tile tile = tileMap[q][w];
				Group tileRep = new Group();
				decorateTile(tile, tileRep, chpt);
				map.add(tileRep, q, w, 1, 1);
			}
		}
		
		Group group = new Group();
		
		Group background = new Group();
		Group nearLeft = new Group();
		Group nearRight = new Group();
		Group farLeft = new Group();
		Group farRight = new Group();
		Group center = new Group();
		ImageView back = new ImageView();
		ImageView nlImage = new ImageView();
		ImageView nrImage = new ImageView();
		ImageView flImage = new ImageView();
		ImageView frImage = new ImageView();
		ImageView middle = new ImageView();
		background.getChildren().add(back);
		farLeft.getChildren().add(flImage);
		nearLeft.getChildren().add(nlImage);
		farRight.getChildren().add(frImage);
		nearRight.getChildren().add(nrImage);
		center.getChildren().add(middle);

		nearLeft.setTranslateX(400);
		nearLeft.setTranslateY(300);
		nearRight.setTranslateX(150);
		nearRight.setTranslateY(300);
		farLeft.setTranslateY(300);
		farRight.setTranslateX(550);
		farRight.setTranslateY(300);
		center.setTranslateX(275);
		center.setTranslateY(300);
		
		ImageView[] stageParts = new ImageView[] {back, nlImage, nrImage, flImage, frImage, middle};
		
		Group nameBox = new Group();
		Rectangle nameRect = new Rectangle(200, 50);
		nameRect.setFill(Color.ALICEBLUE);
		nameBox.getChildren().add(nameRect);
		Label nameLabel = new Label();
		nameLabel.setFont(new Font(20));
		nameBox.setTranslateY(500);
		nameBox.getChildren().add(nameLabel);
		
		Group textBox = new Group();
		Rectangle textRect = new Rectangle(700, 200);
		textRect.setFill(Color.ALICEBLUE);
		textBox.getChildren().add(textRect);
		Label textLabel = new Label();
		textLabel.setMaxWidth(700);
		textLabel.setWrapText(true);
		textLabel.setFont(new Font(30));;
		textLabel.setTranslateY(50);
		textBox.setTranslateY(500);
		textBox.getChildren().add(textLabel);
		
		try {
			dialog.readDialogue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Button next = new Button("NEXT>>");
		EventHandler<MouseEvent> nextLine = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				List<String> part = dialog.nextDialogueComponent();
				if (part == null) {
					((Group)primaryStage.getScene().getRoot()).getChildren().remove(group);
					if (listener != null) {
						listener.setTalkConvo(null, false, null);
					}
					double v = pane.getVvalue();
					double h = pane.getHvalue();
					fillTilesDefault(primaryStage, map, info, click, chpt, pane);
					pane.setVvalue(v);
					pane.setHvalue(h);
				} else {
					for (int q = 0; q < part.size() - 2; q++) {
						try {
							int pic = Integer.parseInt(part.get(q).substring(0, 1));
							String file = part.get(q).substring(2);
							if (pic == 9) {
								if (file.equals("join")) {
									chpt.getAllyUnits().remove(listener);
									chpt.getEnemyUnits().remove(listener);
									chpt.getPlayerUnits().add(listener);
								} else if (file.equals("leave")) {
									chpt.getAllyUnits().remove(listener);
									chpt.getEnemyUnits().remove(listener);
									listenerTile.setOccupant(null);
								} else if (file.equals("give")) {
									if (initiator.getHeldItem() == null) {
										initiator.setHeldItem(listener.getTalkReward());
									} else {
										FEGemWarManager.convoy.add(listener.getTalkReward());
									}
								} else if (file.equals("giveall")) {
									for (int w = 0; w < chpt.getPlayerUnits().size(); w++) {
										chpt.getPlayerUnits().get(w).setHeldItem(listener.getTalkReward().clone());
									}
								}
								continue;
							}
							if (file.equals("clear")) {
								stageParts[pic].setImage(null);
								continue;
							}
							file = "scene_images/" + file;
							if (pic == 0) {
								Image image = new Image(new FileInputStream(file), 700, 500, false, false);
								stageParts[pic].setImage(image);
							} else {
								Image image = new Image(new FileInputStream(file), 150, 200, false, false);
								stageParts[pic].setImage(image);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					String name = part.get(part.size() - 2);
					String speech = part.get(part.size() - 1);
					if (name.equals("_")) {
						name = "";
					}
					if (speech.equals("_")) {
						speech = "";
					}
					nameLabel.setText(name);
					textLabel.setText(speech);
				}
			}
		};
		EventHandler<KeyEvent> pressNextLine = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCharacter().equals("Z")
						|| arg0.getCharacter().equals("z")) {
					nextLine.handle(null);
				}
			}
		};
		next.addEventFilter(MouseEvent.MOUSE_CLICKED, nextLine);
		next.addEventHandler(KeyEvent.KEY_TYPED, pressNextLine);
		
		next.setTranslateX(600);
		next.setTranslateY(650);
		
		
		List<String> part = dialog.nextDialogueComponent();
		for (int q = 0; q < part.size() - 2; q++) {
			try {
				int pic = Integer.parseInt(part.get(q).substring(0, 1));
				String file = part.get(q).substring(2);
				if (pic == 9) {
					if (file.equals("join")) {
						chpt.getAllyUnits().remove(listener);
						chpt.getEnemyUnits().remove(listener);
						chpt.getPlayerUnits().add(listener);
					} else if (file.equals("leave")) {
						chpt.getAllyUnits().remove(listener);
						chpt.getEnemyUnits().remove(listener);
						listenerTile.setOccupant(null);
					} else if (file.equals("give")) {
						if (initiator.getHeldItem() == null) {
							initiator.setHeldItem(listener.getTalkReward());
						} else {
							FEGemWarManager.convoy.add(listener.getTalkReward());
						}
					} else if (file.equals("giveall")) {
						for (int w = 0; w < chpt.getPlayerUnits().size(); w++) {
							chpt.getPlayerUnits().get(w).setHeldItem(listener.getTalkReward().clone());
						}
					}
					continue;
				}
				if (file.equals("clear")) {
					stageParts[pic].setImage(null);
					continue;
				}
				file = "scene_images/" + file;
				if (pic == 0) {
					Image image = new Image(new FileInputStream(file), 700, 500, false, false);
					stageParts[pic].setImage(image);
				} else {
					Image image = new Image(new FileInputStream(file), 150, 200, false, false);
					stageParts[pic].setImage(image);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		String name = part.get(part.size() - 2);
		String speech = part.get(part.size() - 1);
		if (name.equals("_")) {
			name = "";
		}
		if (speech.equals("_")) {
			speech = "";
		}
		nameLabel.setText(name);
		textLabel.setText(speech);

		
		group.getChildren().add(background);
		group.getChildren().add(farLeft);
		group.getChildren().add(nearLeft);
		group.getChildren().add(farRight);
		group.getChildren().add(nearRight);
		group.getChildren().add(center);
		group.getChildren().add(textBox);
		group.getChildren().add(nameBox);
		group.getChildren().add(next);
		
		((Group)primaryStage.getScene().getRoot()).getChildren().add(group);
		primaryStage.show();
	}

	
	public static void performMapEvent(Stage primaryStage, Chapter chpt) {
		
		Group group = new Group();
		Group info = new Group();
		Group click = new Group();
		
		GridPane map = new GridPane();
		ScrollPane pane = new ScrollPane(new Group(map));
		FEGemWarManager.turn = 1;
		fillTilesDefault(primaryStage, map, info, click, chpt, pane);
		
		pane.setMaxWidth(700);
		pane.setMaxHeight(500);
		pane.setPannable(true);
		group.getChildren().add(pane);
		Label objLabel = new Label("Objective: " + chpt.getObjective().getName());
		objLabel.setFont(new Font(20));
		objLabel.setTranslateY(500);
		group.getChildren().add(objLabel);
		info.setTranslateY(550);
		group.getChildren().add(info);
		click.setTranslateX(350);
		click.setTranslateY(500);
		group.getChildren().add(click);
		Scene scene = new Scene(group, 700, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void fillTilesDefault(Stage primaryStage, GridPane map, Group info, Group click,
			Chapter chpt, ScrollPane pane) {
		
		if (chpt.getObjective().checkFailed(chpt)) {
			//TODO GAME OVER SCREEN
		} else if (chpt.getObjective().checkComplete(chpt)) {
			playNextPartOfSequence(primaryStage, chpt);
		}
		
		if (FEGemWarManager.chapterPartIdx < chpt.getAmountInSequence()
				&& chpt.getEvent(FEGemWarManager.chapterPartIdx).getTurn() == FEGemWarManager.turn) {
			StoryEvent se = chpt.getEvent(FEGemWarManager.chapterPartIdx);
			FEGemWarManager.chapterPartIdx++;
			if (se instanceof DialogueEvent) {
				performInMapDialogueEvent(primaryStage, map, info, click, pane,
						(DialogueEvent)se, chpt, null, null, null);
			} else if (se instanceof PickUnitsEvent) {
				//TODO
			} else if (se instanceof ReinforcementsEvent) {
				//TODO
			} else if (se instanceof MapEvent) {
				//TODO
			}
		}
		
		Button endTurn = new Button("End Turn");
		endTurn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				FEGemWarManager.unexhaustAll(chpt.getPlayerUnits());
				System.out.println(chpt.getEnemyUnits().size());
				for (int q = 0; q < chpt.getEnemyUnits().size(); q++) {
					int idx = q;
					Unit u = chpt.getEnemyUnits().get(q);
					if (chpt.getPlayerUnits().contains(u)) {
						System.out.println("Invalid found");
					}
					PauseTransition pause = new PauseTransition(Duration.seconds(4 * idx));
					pause.setOnFinished(e -> {
						Object[] action = FEGemWarManager.performUnitAI(u, chpt);
						if (action != null) {
							AIType ai = (AIType) action[0];
							if (ai == AIType.ATTACK) {
								//TODO move, then attack
								Tile here = (Tile)action[1];
								Tile atkTile = (Tile)action[2];
								Tile dfdTile = (Tile)action[3];
								Unit dfd = dfdTile.getOccupant();
								int[] forecast = CombatManager.getBattleForecast(u, dfd, u.getEquippedWeapon(),
										dfd.getEquippedWeapon(), atkTile, dfdTile);
								
								//TODO animate movement
								FEGemWarManager.moveUnit(u, here, atkTile);
								double vVal = pane.getVvalue();
								double hVal = pane.getHvalue();
								fillTilesEnemy(primaryStage, map, info, click, chpt, pane);
								pane.setVvalue(vVal);
								pane.setHvalue(hVal);
								
								Scene scene = primaryStage.getScene();
								PauseTransition guardPause = new PauseTransition(Duration.seconds(2));
								guardPause.setOnFinished(p -> {
									double v = pane.getVvalue();
									double h = pane.getHvalue();
									fillTilesEnemy(primaryStage, map, info, click, chpt, pane);
									pane.setVvalue(v);
									pane.setHvalue(h);
									primaryStage.setScene(scene);
									primaryStage.show();
								});
								playAttack(primaryStage, map, info, click, chpt, pane, u, dfd,
										atkTile, dfdTile, forecast);
								guardPause.play();
							} else if (ai == AIType.BURN) {
								//TODO move, then burn
							} else if (ai == AIType.GUARD) {
								Tile atkTile = (Tile)action[1];
								Tile dfdTile = (Tile)action[2];
								Unit dfd = dfdTile.getOccupant();
								int[] forecast = CombatManager.getBattleForecast(u, dfd, u.getEquippedWeapon(),
									dfd.getEquippedWeapon(), atkTile, dfdTile);
								
								Scene scene = primaryStage.getScene();
								PauseTransition guardPause = new PauseTransition(Duration.seconds(2));
								guardPause.setOnFinished(p -> {
									double v = pane.getVvalue();
									double h = pane.getHvalue();
									fillTilesEnemy(primaryStage, map, info, click, chpt, pane);
									pane.setVvalue(v);
									pane.setHvalue(h);
									primaryStage.setScene(scene);
									primaryStage.show();
								});
								playAttack(primaryStage, map, info, click, chpt, pane, u, dfd,
										atkTile, dfdTile, forecast);
								guardPause.play();
	
							} else if (ai == AIType.PURSUE) {
								//TODO move, then attack
							}
						}
						double v = pane.getVvalue();
						double h = pane.getHvalue();
						fillTilesEnemy(primaryStage, map, info, click, chpt, pane);
						pane.setVvalue(v);
						pane.setHvalue(h);
						if (idx == chpt.getEnemyUnits().size() - 1) {
							v = pane.getVvalue();
							h = pane.getHvalue();
							fillTilesDefault(primaryStage, map, info, click, chpt, pane);
							pane.setVvalue(v);
							pane.setHvalue(h);
						}
					});
					pause.play();
				}		

			}
		});
		endTurn.setFont(new Font(20));
		click.getChildren().add(endTurn);
		
		map.getChildren().clear();
		Tile[][] tileMap = chpt.getMap().getMap();
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				Tile tile = tileMap[q][w];
				Group tileRep = new Group();
				decorateTile(tile, tileRep, chpt);
				tileRep.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						giveTileInfo(info, tile, chpt);
					}
				});
				if (tile.getOccupant() != null) {
					Unit u = tile.getOccupant();
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								double v = pane.getVvalue();
								double h = pane.getHvalue();
								fillTilesTraversable(primaryStage, map, info, click, chpt, pane, u, tile);
								pane.setVvalue(v);
								pane.setHvalue(h);
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								giveFullUnitInfo(click, u, endTurn);
							}
						}
					});
				}
				map.add(tileRep, q, w, 1, 1);
			}
		}
	}
	public static void fillTilesTraversable(Stage primaryStage, GridPane map, Group info, Group click,
			Chapter chpt, ScrollPane pane, Unit selected, Tile start) {
		map.getChildren().clear();
		HashMap<Tile, Integer> traversable =
				FEGemWarManager.getTraversableTiles(chpt, selected, start);
		HashMap<Tile, Integer> attackable =
				FEGemWarManager.getAttackableTiles(traversable, selected, chpt);
		Tile[][] tileMap = chpt.getMap().getMap();
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				Tile tile = tileMap[q][w];
				Group tileRep = new Group();
				decorateTileTraversable(tile, tileRep, chpt, traversable, attackable);
				tileRep.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						giveTileInfo(info, tile, chpt);
					}
				});
				if (tile.getOccupant() != null) {
					Unit u = tile.getOccupant();
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								if (u == selected && chpt.getPlayerUnits().contains(selected)) {
									double v = pane.getVvalue();
									double h = pane.getHvalue();
									fillTilesOptions(primaryStage, map, info, click, chpt, pane, selected, start, tile);
									pane.setVvalue(v);
									pane.setHvalue(h);
								}
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								giveFullUnitInfo(click, u, null);
							}
						}
					});
				} else {
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								if (traversable.get(tile) != null
										&& chpt.getPlayerUnits().contains(selected)) {
									double v = pane.getVvalue();
									double h = pane.getHvalue();
									fillTilesOptions(primaryStage, map, info, click, chpt, pane, selected, start, tile);
									pane.setVvalue(v);
									pane.setHvalue(h);
								}
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								double v = pane.getVvalue();
								double h = pane.getHvalue();
								fillTilesDefault(primaryStage, map, info, click, chpt, pane);
								pane.setVvalue(v);
								pane.setHvalue(h);
							}
						}
					});
				}
				map.add(tileRep, q, w, 1, 1);
			}
		}
	}
	public static void fillTilesOptions(Stage primaryStage, GridPane map, Group info, Group click,
			Chapter chpt, ScrollPane pane, Unit selected, Tile start, Tile dest) {
		map.getChildren().clear();
		HashMap<Tile, Integer> attackable =
				FEGemWarManager.getAttackableBattlegroundTilesFromDestination(chpt, selected, dest);
		Tile[][] tileMap = chpt.getMap().getMap();
		info.getChildren().clear();
		GridPane optionsPane = new GridPane();
		List<Tile> talkable = 
				FEGemWarManager.getTalkableTiles(dest, chpt, selected);
		if (!talkable.isEmpty()) {
			Button talk = new Button("Talk");
			talk.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					FEGemWarManager.moveUnit(selected, start, dest);
					double v = pane.getVvalue();
					double h = pane.getHvalue();
					fillTilesChooseTalker(primaryStage, map, info, click, chpt, pane,
							selected, start, dest, talkable);
					pane.setVvalue(v);
					pane.setHvalue(h);
				}
			});
			optionsPane.add(talk, 0, 0);
		}
		List<Tile> reallyAttackable =
				FEGemWarManager.getAttackableTilesWithEnemies(attackable, chpt, selected);
		if (!reallyAttackable.isEmpty()) {
			Button attack = new Button("Attack");
			attack.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					double v = pane.getVvalue();
					double h = pane.getHvalue();
					fillTilesChooseTarget(primaryStage, map, info, click, chpt, pane, selected, start, dest);
					pane.setVvalue(v);
					pane.setHvalue(h);
				}
			});
			optionsPane.add(attack, 0, 1);
		}
		if (dest.getType() == TileType.WARP_TILE) {
			Button escape = new Button("Escape");
			escape.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					//TODO play an animation for warping
					FEGemWarManager.escapeUnit(chpt, start, selected);
					double v = pane.getVvalue();
					double h = pane.getHvalue();
					fillTilesDefault(primaryStage, map, info, click, chpt, pane);
					pane.setVvalue(v);
					pane.setHvalue(h);
				}
			});
			optionsPane.add(escape, 0, 2);
		}
		if (dest.getType() == TileType.SEIZE_TILE
				&& chpt.getObjective() instanceof SeizeObjective
				&& selected.isLeader()) {
			Button seize = new Button("Seize");
			seize.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					FEGemWarManager.moveUnit(selected, start, dest);
					//TODO play seize animation
					chpt.setSeized(true);
					double v = pane.getVvalue();
					double h = pane.getHvalue();
					fillTilesDefault(primaryStage, map, info, click, chpt, pane);
					pane.setVvalue(v);
					pane.setHvalue(h);
				}
			});
			optionsPane.add(seize, 0, 3);
		}
		if (selected.getSpecialItem() instanceof UsableItem
				|| selected.getHeldItem() instanceof UsableItem) {
			Button item = new Button("Item");
			item.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
				}
			});
			optionsPane.add(item, 0, 4);
		}
		if (selected.getSpecialItem() instanceof Weapon
				|| selected.getHeldItem() instanceof Weapon) {
			Button equip = new Button("Equip");
			equip.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					giveEquipOptions(primaryStage, map, info, click, selected, chpt, pane, start, dest);
				}
			});
			optionsPane.add(equip, 0, 5);
		}
		//TODO special skill option
		Button wait = new Button("Wait");
		wait.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				FEGemWarManager.moveUnit(selected, start, dest);
				info.getChildren().clear();
				double v = pane.getVvalue();
				double h = pane.getHvalue();
				fillTilesDefault(primaryStage, map, info, click, chpt, pane);
				pane.setVvalue(v);
				pane.setHvalue(h);
			}
		});
		optionsPane.add(wait, 0, 6);
		info.getChildren().add(optionsPane);
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				Tile tile = tileMap[q][w];
				Group tileRep = new Group();
				decorateTileOptions(tile, tileRep, chpt, selected, dest, attackable);
				if (tile.getOccupant() != null) {
					Unit u = tile.getOccupant();
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								if (attackable.get(tile) != null) {
									//TODO
								}
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								giveFullUnitInfo(click, u, null);
							}
						}
					});
				} else {
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								//TODO
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								double v = pane.getVvalue();
								double h = pane.getHvalue();
								fillTilesTraversable(primaryStage, map, info, click, chpt, pane, selected, start);
								pane.setVvalue(v);
								pane.setHvalue(h);
							}
						}
					});
				}
				map.add(tileRep, q, w, 1, 1);
			}
		}
	}
	public static void fillTilesChooseTarget(Stage primaryStage, GridPane map, Group info, Group click,
			Chapter chpt, ScrollPane pane, Unit selected, Tile start, Tile dest) {
		map.getChildren().clear();
		HashMap<Tile, Integer> attackable =
				FEGemWarManager.getAttackableBattlegroundTilesFromDestination(chpt, selected, dest);
		Tile[][] tileMap = chpt.getMap().getMap();
		
		info.getChildren().clear();
		
		List<Tile> reallyAttackable =
				FEGemWarManager.getAttackableTilesWithEnemies(attackable, chpt, selected);
		
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				Tile tile = tileMap[q][w];
				Group tileRep = new Group();
				decorateTileOptions(tile, tileRep, chpt, selected, dest, attackable);
				if (tile.getOccupant() != null) {
					Unit u = tile.getOccupant();
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								if (reallyAttackable.contains(tile)) {
									makeBattleForecast(primaryStage, map, info, click, chpt, pane, selected,
											tile.getOccupant(), start, dest, tile);
								}
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								giveFullUnitInfo(click, u, null);
							}
						}
					});
				} else {
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								//nothing
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								double v = pane.getVvalue();
								double h = pane.getHvalue();
								fillTilesOptions(primaryStage, map, info, click, chpt, pane, selected, start, dest);
								pane.setVvalue(v);
								pane.setHvalue(h);
							}
						}
					});
				}
				map.add(tileRep, q, w, 1, 1);
			}
		}
	}
	
	public static void fillTilesChooseTalker(Stage primaryStage, GridPane map, Group info, Group click,
			Chapter chpt, ScrollPane pane, Unit selected, Tile start, Tile dest, List<Tile> talkable) {
		// TODO Auto-generated method stub
		Tile[][] tileMap = chpt.getMap().getMap();
		info.getChildren().clear();
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				Tile tile = tileMap[q][w];
				Group tileRep = new Group();
				decorateTileOptionsTalk(tile, tileRep, chpt, selected, dest, talkable);
				if (tile.getOccupant() != null) {
					Unit u = tile.getOccupant();
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								if (talkable.contains(tile)) {
									performInMapDialogueEvent(primaryStage, map, info, click, pane,
											u.getTalkConvo(), chpt, selected, u, tile);
								}
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								giveFullUnitInfo(click, u, null);
							}
						}
					});
				} else {
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.PRIMARY) {
								//nothing
							} else if (arg0.getButton() == MouseButton.SECONDARY) {
								double v = pane.getVvalue();
								double h = pane.getHvalue();
								fillTilesOptions(primaryStage, map, info, click, chpt, pane, selected, start, dest);
								pane.setVvalue(v);
								pane.setHvalue(h);
							}
						}
					});
				}
				map.add(tileRep, q, w, 1, 1);
			}
		}

	}

	public static void fillTilesEnemy(Stage primaryStage, GridPane map, Group info, Group click,
			Chapter chpt, ScrollPane pane) {
		
		if (chpt.getObjective().checkFailed(chpt)) {
			//TODO GAME OVER SCREEN
		} else if (chpt.getObjective().checkComplete(chpt)) {
			playNextPartOfSequence(primaryStage, chpt);
		}
		
		map.getChildren().clear();
		click.getChildren().clear();
		Tile[][] tileMap = chpt.getMap().getMap();
		for (int q = 0; q < tileMap.length; q++) {
			for (int w = 0; w < tileMap[0].length; w++) {
				Tile tile = tileMap[q][w];
				Group tileRep = new Group();
				decorateTileEnemy(tile, tileRep, chpt);
				tileRep.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						giveTileInfo(info, tile, chpt);
					}
				});
				if (tile.getOccupant() != null) {
					Unit u = tile.getOccupant();
					tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							if (arg0.getButton() == MouseButton.SECONDARY) {
								giveFullUnitInfo(click, u, null);
							}
						}
					});
				}
				map.add(tileRep, q, w, 1, 1);
			}
		}
	}

	public static void decorateTile(Tile tile, Group rep, Chapter chpt) {
		String filename = chpt.getDesign().get(tile.getType());
		if (filename == null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(Color.LAWNGREEN);
			rep.getChildren().add(rect);
		} else {
			try {
				Image image = new Image(new FileInputStream("tiles/" + filename), 50, 50, false, false);
				ImageView view = new ImageView(image);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (tile.getOccupant() != null) {
			try {
				Unit u = tile.getOccupant();
				Image image = new Image(new FileInputStream("sprites/" + u.getName() + ".jpg"),
						35, 35, false, false);
				ImageView view = new ImageView(image);
				Rectangle rect = new Rectangle(40, 40);
				if (chpt.getPlayerUnits().contains(u)) {
					rect.setFill(Color.BLUE);
				} else if (chpt.getEnemyUnits().contains(u)) {
					rect.setFill(Color.RED);
				} else if (chpt.getAllyUnits().contains(u)) {
					rect.setFill(Color.GREEN);
				}
				rect.setTranslateX(5);
				rect.setTranslateY(5);
				rep.getChildren().add(rect);
				view.setTranslateX(7.5);
				view.setTranslateY(7.5);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static void decorateTileTraversable(Tile tile, Group rep, Chapter chpt,
			HashMap<Tile, Integer> traversable, HashMap<Tile, Integer> attackable) {
		String filename = chpt.getDesign().get(tile.getType());
		if (filename == null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(Color.LAWNGREEN);
			rep.getChildren().add(rect);
		} else {
			try {
				Image image = new Image(new FileInputStream("tiles/" + filename), 50, 50, false, false);
				ImageView view = new ImageView(image);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (traversable.get(tile) != null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(new Color(0, 0, 1, 0.25));
			rep.getChildren().add(rect);
		} else if (attackable.get(tile) != null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(new Color(1, 0, 0, 0.25));
			rep.getChildren().add(rect);
		}
		if (tile.getOccupant() != null) {
			try {
				Unit u = tile.getOccupant();
				Image image = new Image(new FileInputStream("sprites/" + u.getName() + ".jpg"),
						35, 35, false, false);
				ImageView view = new ImageView(image);
				Rectangle rect = new Rectangle(40, 40);
				if (chpt.getPlayerUnits().contains(u)) {
					rect.setFill(Color.BLUE);
				} else if (chpt.getEnemyUnits().contains(u)) {
					rect.setFill(Color.RED);
				} else if (chpt.getAllyUnits().contains(u)) {
					rect.setFill(Color.GREEN);
				}
				rect.setTranslateX(5);
				rect.setTranslateY(5);
				rep.getChildren().add(rect);
				view.setTranslateX(7.5);
				view.setTranslateY(7.5);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
	public static void decorateTileOptions(Tile tile, Group rep, Chapter chpt, Unit selected, Tile dest,
			HashMap<Tile, Integer> attackable) {
		String filename = chpt.getDesign().get(tile.getType());
		if (filename == null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(Color.LAWNGREEN);
			rep.getChildren().add(rect);
		} else {
			try {
				Image image = new Image(new FileInputStream("tiles/" + filename), 50, 50, false, false);
				ImageView view = new ImageView(image);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (attackable.get(tile) != null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(new Color(1, 0, 0, 0.25));
			rep.getChildren().add(rect);
		}
		if (tile.getOccupant() != null) {
			try {
				Unit u = tile.getOccupant();
				Image image = new Image(new FileInputStream("sprites/" + u.getName() + ".jpg"),
						35, 35, false, false);
				ImageView view = new ImageView(image);
				Rectangle rect = new Rectangle(40, 40);
				if (chpt.getPlayerUnits().contains(u)) {
					rect.setFill(Color.BLUE);
				} else if (chpt.getEnemyUnits().contains(u)) {
					rect.setFill(Color.RED);
				} else if (chpt.getAllyUnits().contains(u)) {
					rect.setFill(Color.GREEN);
				}
				rect.setTranslateX(5);
				rect.setTranslateY(5);
				rep.getChildren().add(rect);
				view.setTranslateX(7.5);
				view.setTranslateY(7.5);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (tile == dest) {
			try {
				Image image = new Image(new FileInputStream("sprites/" + selected.getName() + ".jpg"),
						35, 35, false, false);
				ImageView view = new ImageView(image);
				Rectangle rect = new Rectangle(40, 40);
				if (chpt.getPlayerUnits().contains(selected)) {
					rect.setFill(Color.AQUA);
				} else if (chpt.getEnemyUnits().contains(selected)) {
					rect.setFill(Color.CORAL);
				} else if (chpt.getAllyUnits().contains(selected)) {
					rect.setFill(Color.GREENYELLOW);
				}
				rect.setTranslateX(5);
				rect.setTranslateY(5);
				rep.getChildren().add(rect);
				view.setTranslateX(7.5);
				view.setTranslateY(7.5);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static void decorateTileOptionsTalk(Tile tile, Group rep, Chapter chpt,
			Unit selected, Tile dest, List<Tile> talkable) {
		String filename = chpt.getDesign().get(tile.getType());
		if (filename == null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(Color.LAWNGREEN);
			rep.getChildren().add(rect);
		} else {
			try {
				Image image = new Image(new FileInputStream("tiles/" + filename), 50, 50, false, false);
				ImageView view = new ImageView(image);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (talkable.contains(tile)) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(new Color(0, 1, 1, 0.25));
			rep.getChildren().add(rect);
		}
		if (tile.getOccupant() != null) {
			try {
				Unit u = tile.getOccupant();
				Image image = new Image(new FileInputStream("sprites/" + u.getName() + ".jpg"),
						35, 35, false, false);
				ImageView view = new ImageView(image);
				Rectangle rect = new Rectangle(40, 40);
				if (chpt.getPlayerUnits().contains(u)) {
					rect.setFill(Color.BLUE);
				} else if (chpt.getEnemyUnits().contains(u)) {
					rect.setFill(Color.RED);
				} else if (chpt.getAllyUnits().contains(u)) {
					rect.setFill(Color.GREEN);
				}
				rect.setTranslateX(5);
				rect.setTranslateY(5);
				rep.getChildren().add(rect);
				view.setTranslateX(7.5);
				view.setTranslateY(7.5);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (tile == dest) {
			try {
				Image image = new Image(new FileInputStream("sprites/" + selected.getName() + ".jpg"),
						35, 35, false, false);
				ImageView view = new ImageView(image);
				Rectangle rect = new Rectangle(40, 40);
				if (chpt.getPlayerUnits().contains(selected)) {
					rect.setFill(Color.AQUA);
				} else if (chpt.getEnemyUnits().contains(selected)) {
					rect.setFill(Color.CORAL);
				} else if (chpt.getAllyUnits().contains(selected)) {
					rect.setFill(Color.GREENYELLOW);
				}
				rect.setTranslateX(5);
				rect.setTranslateY(5);
				rep.getChildren().add(rect);
				view.setTranslateX(7.5);
				view.setTranslateY(7.5);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static void decorateTileEnemy(Tile tile, Group rep, Chapter chpt) {
		String filename = chpt.getDesign().get(tile.getType());
		if (filename == null) {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(Color.LAWNGREEN);
			rep.getChildren().add(rect);
		} else {
			try {
				Image image = new Image(new FileInputStream("tiles/" + filename), 50, 50, false, false);
				ImageView view = new ImageView(image);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (tile.getOccupant() != null) {
			try {
				Unit u = tile.getOccupant();
				Image image = new Image(new FileInputStream("sprites/" + u.getName() + ".jpg"),
						35, 35, false, false);
				ImageView view = new ImageView(image);
				Rectangle rect = new Rectangle(40, 40);
				if (chpt.getPlayerUnits().contains(u)) {
					rect.setFill(Color.BLUE);
				} else if (chpt.getEnemyUnits().contains(u)) {
					rect.setFill(Color.RED);
				} else if (chpt.getAllyUnits().contains(u)) {
					rect.setFill(Color.GREEN);
				}
				rect.setTranslateX(5);
				rect.setTranslateY(5);
				rep.getChildren().add(rect);
				view.setTranslateX(7.5);
				view.setTranslateY(7.5);
				rep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void giveTileInfo(Group info, Tile tile, Chapter chpt) {
		info.getChildren().clear();
		Rectangle tileRect = new Rectangle(175, 150);
		tileRect.setFill(Color.AQUAMARINE);
		Label tileName = new Label("Tile: " + tile.getName());
		tileName.setFont(new Font(15));
		Label tileAvo = new Label("AVO Bonus: " + tile.getType().getAvoidanceBonus() + "%");
		tileAvo.setFont(new Font(15));
		tileAvo.setTranslateY(100);
		info.getChildren().add(tileRect);
		info.getChildren().add(tileName);
		info.getChildren().add(tileAvo);
		
		if (tile.getOccupant() != null) {
			Unit u = tile.getOccupant();
			Rectangle unitRect = new Rectangle(175, 150);
			unitRect.setFill(Color.CORAL);
			unitRect.setTranslateX(175);
			Label unitName = new Label("Name: " + u.getName());
			unitName.setFont(new Font(15));
			unitName.setTranslateX(175);
			Label unitHP = new Label("HP: " + u.getCurrentHP() + "/" + u.getMaxHP());
			unitHP.setFont(new Font(15));
			unitHP.setTranslateX(175);
			unitHP.setTranslateY(100);
			info.getChildren().add(unitRect);
			info.getChildren().add(unitName);
			info.getChildren().add(unitHP);
		}
	}
	public static void giveFullUnitInfo(Group click, Unit unit, Button endTurn) {
		click.getChildren().clear();
		System.out.println(unit.getName());
		if (endTurn != null) {
			click.getChildren().add(endTurn);
		}
		Rectangle info = new Rectangle(350, 150);
		info.setTranslateY(50);
		info.setFill(Color.AQUA);
		Label text = new Label("Info for " + unit.getName());
		text.setTranslateY(50);
		click.getChildren().add(info);
		click.getChildren().add(text);
		//TODO
	}
	public static void giveEquipOptions(Stage primaryStage, GridPane map, Group info, Group click, Unit selected,
			Chapter chpt, ScrollPane pane, Tile start, Tile dest) {
		info.getChildren().clear();
		GridPane opts = new GridPane();
		if (selected.getSpecialItem() instanceof Weapon) {
			Rectangle rect = new Rectangle(350, 50);
			rect.setFill(Color.CHARTREUSE);
			Label name = new Label(selected.getSpecialItem().getName());
			rect.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					selected.equipSpecial();
					double v = pane.getVvalue();
					double h = pane.getHvalue();
					fillTilesOptions(primaryStage, map, info, click, chpt, pane, selected, start, dest);
					pane.setVvalue(v);
					pane.setHvalue(h);
				}
			});
			Group special = new Group();
			special.getChildren().add(rect);
			special.getChildren().add(name);
			opts.add(special, 0, 0);
		}
		if (selected.getHeldItem() instanceof Weapon
				&& ((Weapon)selected.getHeldItem()).typeId() == selected.heldWeaponType()) {
			Rectangle rect = new Rectangle(350, 50);
			rect.setFill(Color.VIOLET);
			Label name = new Label(selected.getHeldItem().getName());
			rect.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					selected.equipHeld();
					double v = pane.getVvalue();
					double h = pane.getHvalue();
					fillTilesOptions(primaryStage, map, info, click, chpt, pane, selected, start, dest);
					pane.setVvalue(v);
					pane.setHvalue(h);
				}
			});
			Group held = new Group();
			held.getChildren().add(rect);
			held.getChildren().add(name);
			opts.add(held, 0, 1);
		}
		Rectangle rect = new Rectangle(350, 50);
		rect.setFill(Color.AQUAMARINE);
		Label name = new Label("None");
		rect.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				selected.equipNone();
				double v = pane.getVvalue();
				double h = pane.getHvalue();
				fillTilesOptions(primaryStage, map, info, click, chpt, pane, selected, start, dest);
				pane.setVvalue(v);
				pane.setHvalue(h);
				System.out.println("Done");
			}
		});
		Group none = new Group();
		none.getChildren().add(rect);
		none.getChildren().add(name);
		opts.add(none, 0, 2);
		
		info.getChildren().add(opts);

	}
	public static void makeBattleForecast(Stage primaryStage, GridPane map,
			Group info, Group click, Chapter chpt, ScrollPane pane, Unit atk,
			Unit dfd, Tile start, Tile dest, Tile target) {
		info.getChildren().clear();
		
		int distance = Math.abs(start.getX() - dest.getX()) + Math.abs(start.getY() - dest.getY());
		atk.equipForDistance(distance);
		
		Weapon atkWep = atk.getEquippedWeapon();
		Weapon dfdWep = dfd.getEquippedWeapon();
		int[] forecast = CombatManager.getBattleForecast(atk, dfd, atkWep,
				dfdWep, dest, target);
		
		Rectangle playerRect = new Rectangle(125, 150);
		playerRect.setFill(Color.AQUA);
		Label playerWeapon = new Label();
		if (atkWep != null) {
			playerWeapon.setText(atkWep.getName());
		}
		Label playerHP = new Label("" + forecast[0]);
		Label playerMight = new Label("" + forecast[1]);
		int num = forecast[4] * forecast[5];
		if (num != 1) {
			playerMight.setText(playerMight.getText() + " x" + num);
		}
		Label playerHit = new Label("" + forecast[2]);
		Label playerCrit = new Label("" + forecast[3]);
		
		playerHP.setTranslateX(100);
		playerHP.setTranslateY(30);
		playerMight.setTranslateX(100);
		playerMight.setTranslateY(60);
		playerHit.setTranslateX(100);
		playerHit.setTranslateY(90);
		playerCrit.setTranslateX(100);
		playerCrit.setTranslateY(120);

		Rectangle statRect = new Rectangle(100, 150);
		statRect.setFill(Color.ANTIQUEWHITE);
		Button attack = new Button("Attack");
		attack.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				FEGemWarManager.moveUnit(atk, start, dest);
				Scene scene = primaryStage.getScene();
				PauseTransition pause = new PauseTransition(Duration.seconds(4));
				pause.setOnFinished(e -> {
					fillTilesDefault(primaryStage, map, info, click, chpt, pane);
					primaryStage.setScene(scene);
					primaryStage.show();
				});
				playAttack(primaryStage, map, info, click, chpt, pane, atk, dfd,
						dest, target, forecast);
				pause.play();

			}
		});
		Label hp = new Label("HP");
		Label mt = new Label("Might");
		Label ht = new Label("Hit");
		Label ct = new Label("Critical");
		statRect.setTranslateX(125);
		attack.setTranslateX(125);
		hp.setTranslateX(125);
		hp.setTranslateY(30);
		mt.setTranslateX(125);
		mt.setTranslateY(60);
		ht.setTranslateX(125);
		ht.setTranslateY(90);
		ct.setTranslateX(125);
		ct.setTranslateY(120);
		
		Rectangle enemyRect = new Rectangle(125, 150);
		enemyRect.setFill(Color.ORANGERED);
		Label enemyWeapon = new Label();
		if (dfdWep != null) {
			enemyWeapon.setText(dfdWep.getName());
		}
		Label enemyHP = new Label("" + forecast[6]);
		Label enemyMight = new Label("" + forecast[7]);
		int num2 = forecast[10] * forecast[11];
		if (num2 != 1) {
			enemyMight.setText(enemyMight.getText() + " x" + num2);
		}
		Label enemyHit = new Label("" + forecast[8]);
		Label enemyCrit = new Label("" + forecast[9]);
		
		enemyRect.setTranslateX(225);
		enemyWeapon.setTranslateX(225);
		enemyHP.setTranslateX(225);
		enemyHP.setTranslateY(30);
		enemyMight.setTranslateX(225);
		enemyMight.setTranslateY(60);
		enemyHit.setTranslateX(225);
		enemyHit.setTranslateY(90);
		enemyCrit.setTranslateX(225);
		enemyCrit.setTranslateY(120);

		info.getChildren().add(playerRect);
		info.getChildren().add(playerWeapon);
		info.getChildren().add(playerHP);
		info.getChildren().add(playerMight);
		info.getChildren().add(playerHit);
		info.getChildren().add(playerCrit);
		info.getChildren().add(statRect);
		info.getChildren().add(attack);
		info.getChildren().add(hp);
		info.getChildren().add(mt);
		info.getChildren().add(ht);
		info.getChildren().add(ct);
		info.getChildren().add(enemyRect);
		info.getChildren().add(enemyWeapon);
		info.getChildren().add(enemyHP);
		info.getChildren().add(enemyMight);
		info.getChildren().add(enemyHit);
		info.getChildren().add(enemyCrit);
	}
	public static  void playAttack(Stage primaryStage, GridPane map, Group info,
			Group click, Chapter chpt, ScrollPane pane,
			Unit atk, Unit dfd, Tile dest, Tile target,
			int[] forecast) {
		
		int[] sequence = CombatManager.decideBattle(forecast);
		Group fullDisplay = new Group();
		try {
			Image atkTile = new Image(new FileInputStream("tiles/" + chpt.getDesign().get(dest.getType())),
					350, 100, false, false);
			Image dfdTile = new Image(new FileInputStream("tiles/" + chpt.getDesign().get(target.getType())),
					350, 100, false, false);
			ImageView atkTileView = new ImageView(atkTile);
			ImageView dfdTileView = new ImageView(dfdTile);
			atkTileView.setTranslateY(400);
			dfdTileView.setTranslateX(350);
			dfdTileView.setTranslateY(400);
			
			Image atkImage = new Image(new FileInputStream("sprites/" + atk.getName() + ".jpg"),
					100, 200, false, false);
			Image dfdImage = new Image(new FileInputStream("sprites/" + dfd.getName() + ".jpg"),
					100, 200, false, false);
			ImageView atkView = new ImageView(atkImage);
			ImageView dfdView = new ImageView(dfdImage);
			atkView.setTranslateX(125);
			atkView.setTranslateY(250);
			dfdView.setTranslateX(475);
			dfdView.setTranslateY(250);
			
			//TODO add stats
			
			fullDisplay.getChildren().add(atkTileView);
			fullDisplay.getChildren().add(dfdTileView);
			fullDisplay.getChildren().add(atkView);
			fullDisplay.getChildren().add(dfdView);

			Scene tempScene = new Scene(fullDisplay, 700, 700);
			primaryStage.setScene(tempScene);
			primaryStage.show();
			//TODO figure out why the scene only switches after all of the pauses
			//Hint: when I tried to switch the scene's root instead, the same thing
			//happened
			
			int attackerIdx = 0;
			int hitIdx = 1;
//			int specialIdx = 2;
			while (atk.isAlive() && dfd.isAlive() && attackerIdx < sequence.length) {
				//TODO decide what to do about special skills
				if (sequence[attackerIdx] == 0) {
					if (sequence[hitIdx] == 0) {
						//TODO play atk's attack animation
						//TODO play dfd's dodge animation
					} else if (sequence[hitIdx] == 1) {
						//TODO play atk's attack animation
						//TODO play dfd's getting hit animation
						CombatManager.performAttack(dfd, forecast[1], false);
					} else if (sequence[hitIdx] == 2) {
						//TODO play atk's critical animation
						//TODO play dfd's getting hit animation
						CombatManager.performAttack(dfd, forecast[1], true);
					}
				} else if (sequence[attackerIdx] == 1) {
					if (sequence[hitIdx] == 0) {
						//TODO play dfd's attack animation
						//TODO play atk's dodge animation
					} else if (sequence[hitIdx] == 1) {
						//TODO play dfd's attack animation
						//TODO play atk's getting hit animation
						CombatManager.performAttack(atk, forecast[7], false);
					} else if (sequence[hitIdx] == 2) {
						//TODO play dfd's critical animation
						//TODO play atk's getting hit animation
						CombatManager.performAttack(atk, forecast[7], true);
					}
				}
				attackerIdx += 3;
				hitIdx += 3;
//				specialIdx += 3;
			}
			if (attackerIdx < sequence.length) {
				sequence[attackerIdx] = -1;
			}
			Unit playerUnit = null;
			Unit enemyUnit = null;
			if (chpt.getPlayerUnits().contains(atk)) {
				playerUnit = atk;
				enemyUnit = dfd;
			} else if (chpt.getPlayerUnits().contains(dfd)) {
				playerUnit = dfd;
				enemyUnit = atk;
			}
			if (!atk.isAlive()) {
				FEGemWarManager.killUnit(chpt, atk, dest);
			} else if (!dfd.isAlive()) {
				FEGemWarManager.killUnit(chpt, dfd, target);
			}
			if (playerUnit == null) {
				return;
			}
			if (playerUnit.isAlive()) {
				int exp = Math.max(0, 10 + enemyUnit.getLevel() - playerUnit.getLevel());
				if (!enemyUnit.isAlive()) {
					exp += Math.max(0, 30 + enemyUnit.getLevel() - playerUnit.getLevel());
				}
				boolean[] grown = playerUnit.addExperience(exp);
				//TODO announce experience given
				if (grown != null) {
					//TODO announce level and grown stats
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	

	public static void performIntroSequence(Stage primaryStage) {
		//TODO play animation, then give options
		
		Chapter chpt1 = FEGemWarManager.loadChapter(FEGemWarManager.chapterIdx);
		playNextPartOfSequence(primaryStage, chpt1);
	}
}
