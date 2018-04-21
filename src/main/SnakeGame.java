package main;

import lib.*;

import java.util.List;
import java.util.Random;

public class SnakeGame extends AbstractGame {

    private Random random = new Random();
    private Map map = new Map();
    private Snake snake = new Snake( 10 , 10 );
    private Food food = new Food( 15 , 15 );
    
    public Map getMap() {
    		return map;
    }
    
    public Snake getSnake() {
    		return snake;
    }
    
    public Food getFood() {
    		return food;
    }
    
    public SnakeGame() {
        for(Block b : snake.getBody()) {
            map.addBlock(b);
        }
        map.addBlock(food);
    }

    public int getMapSize() {
        return map.getSize();
    }

    public List<Block> getBlocks() {
        return map.getBlocks();
    }

    @Override
    protected void gameLogic() {
        snake.move();
        // Check if snake eat food
        for(Block b : snake.getBody()) {
            if(b.overlapped(food)) {
                Block newBlock = snake.expand();
                map.addBlock(newBlock);
                food.setX(random.nextInt(map.getSize()));
                food.setY(random.nextInt(map.getSize()));
                break;
            }
        }
        // Check if snake hit itself
        if(snake.hitItself()) {
            end();
        }
    }
    
    public void load( Memento m ) {
    		map.getBlocks().clear();
    		snake = m.getSnake();
    		for( Block b : snake.getBody() ) {
    			map.addBlock( b );
    		}
    		food = m.getFood();
    		map.addBlock( new Block( food.getX() , food.getY() ) );    		
    }
    
    public Memento save() {
    		Memento m = new Memento( snake , food );
    		return m;
    }

    @Override
    protected void handleLeftKey() {
        snake.setDx(-1);
        snake.setDy(0);
    }

    @Override
    protected void handleRightKey() {
        snake.setDx(1);
        snake.setDy(0);
    }

    @Override
    protected void handleUpKey() {
        snake.setDx(0);
        snake.setDy(-1);
    }

    @Override
    protected void handleDownKey() {
        snake.setDx(0);
        snake.setDy(1);
    }
    
    static class Memento {
    	
    		private Snake snake;
    		private Food food;
    		
    		public Memento( Snake inputSnake , Food inputFood ){
    			
    			// Snake
    			snake = new Snake();
    			snake.setDx( inputSnake.getDx() );
    			snake.setDy( inputSnake.getDy() );
    			for( Block b : inputSnake.getBody() ) {
    				snake.getBody().add( new Block( b.getX() , b.getY() ) );
    			}
    			
    			// Food
    			food = new Food( inputFood.getX() , inputFood.getY() );
    		}
    		
    		public Snake getSnake() {
    			return snake;
    		}
    		
    		public Food getFood() {
    			return food;
    		}
    	
    }

}
