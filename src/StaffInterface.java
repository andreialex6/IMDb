public interface StaffInterface {
    public void addProductionSystem(Production p);
    public void addActorSystem(Actor a);
    public void removeProductionSystem(String name);
    public void removeActorSystem(String name);
    public void updateProduction(Production p, String description);
    public void updateActor(Actor a, String biography);
}
