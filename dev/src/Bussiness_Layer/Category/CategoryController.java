package Bussiness_Layer.Category;


import DataLayer.DalObjects.CategoryHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private List<Category> catLst;
    private CategoryHandler _cHandler;
    private List<String> catNames;

    public CategoryController() throws SQLException {
        _cHandler = new CategoryHandler();
        catLst = new ArrayList<Category>();
        catNames = new ArrayList<String>();
        _cHandler.createCategoryTable();
    }

    //clst goes from main father all the way down
    public boolean checkIfTreeExists(List<String> clst)
    {
        List<Category> cat = this.catLst;
        for(int i =0; i<clst.size();i++) {
            boolean flag = false;
            for (int j = 0; j < cat.size(); j++) {
                if (clst.get(i).equals(cat.get(j).getCatName())) {
                    flag = true;
                    cat = cat.get(j).get_sonCat();
                    break;
                }
            }
            if (!flag)
                return false;
        }
        return true;
    }

    //adds in queue mode
    public boolean AddNewCategory(List<String> ls)
    {
        Category temp = new Category(ls.get(0));
        for(int j =0; j<catLst.size();j++)
        {
            if(temp.Compare(catLst.get(j)))
            {
                return AddSonsToFather(j,ls);
            }
        }

        // Father category was not found, need to create from zero
        catLst.add(temp);
        Category temp2 = temp;
        for(int i=1; i<ls.size(); i++)
        {
            temp2.AddNewSonCategory(ls.get(i));
            _cHandler.insert(temp2);
            temp2 = temp2.findSon(ls.get(i));
        }
        return true;


    }
    public void AddNewCategory(String s)
    {
        Category cat = new Category(s);
        catLst.add(cat);
        _cHandler.insert(cat);
    }

    public void InsertFatherCategoryWithSons(List<String> ls)
    {
        if(!ls.isEmpty())
        {
            String newFatherName = ls.get(0);
            Category newFather = new Category(newFatherName);
            _cHandler.insert(newFather);
            for(int i = 1; i<ls.size();i++) {
                newFather.AddNewSonCategory(ls.get(i));
                _cHandler.insert(newFather.findSon(ls.get(i)));
            }
            this.catLst.add(newFather);


        }
    }

    private boolean AddSonsToFather(int index, List<String> ls)
    {
        Category temp = catLst.get(index);
        Category temp2 = temp;
        boolean flag = false;
        for(int i = 1; i<ls.size();i++)
        {
            temp2 = temp.findSon(ls.get(i));

            if(temp2 ==null)
            {
                for(int j=i; j<ls.size(); j++)
                {
                    flag = true;
                    temp.AddNewSonCategory(ls.get(j));
                    temp = temp.findSon(ls.get(j));
                    _cHandler.insert(temp);
                }
                break;
            }
            else
                temp = temp2;
        }
        return flag;
    }

    public void LoadCategories() {
        catLst = _cHandler.loadCategory();
    }

    public List<String> getAllSubCategory(String s) {
        List<String> lst = new ArrayList<String>();
        for(int i = 0; i< catLst.size(); i++) {
            makeListForReport(catLst.get(i), s, lst);
            if(!lst.isEmpty())
                return lst;
        }
        return lst;
    }

    private List<String> makeListForReport(Category cat, String s, List<String> ls)
    {
        if(cat.getCatName().equals(s))
        {
            ls.add(cat.getCatName());
            makeSonsForReport(cat,ls);
            return ls;
        }
        else
        {
            for(int i = 0; i<cat.get_sonCat().size();i++)
            {
                List<String> temp = makeListForReport(cat.get_sonCat().get(i),s,ls);
                if(!temp.isEmpty())
                    return ls;
            }
        }

        return ls;
    }
    private void makeSonsForReport(Category cat, List<String> ls) {
        for(int i =0; i<cat.get_sonCat().size();i++)
        {
            ls.add(cat.get_sonCat().get(i).getCatName());
            makeSonsForReport(cat.get_sonCat().get(i),ls);
        }
    }

    private boolean findSingleCategory(String oldCat, String newCat, List<Category> catList)
    {
        boolean flag = false;
        List<Category> tempLst = catList;
        for (int i = 0; i < tempLst.size(); i++) {
            if(flag)
                return true;
            Category c = tempLst.get(i).findSon(oldCat);
            if (c != null) {
                c.UpdateCategoryName(newCat);
                _cHandler.updateFull(oldCat,c);
                return true;
            }
            else
                flag = findSingleCategory(oldCat,newCat,tempLst.get(i).get_sonCat());
        }
        return false;
    }
    public boolean updateSingleCategory(String oldCat, String newCat) {

        for(int i = 0; i<catLst.size(); i++)
        {
            if(catLst.get(i).getCatName().equals(oldCat))
            {
                catLst.get(i).UpdateCategoryName(newCat);
                _cHandler.updateFull(oldCat,catLst.get(i));
                return true;
            }
        }
        //in case not father
        return findSingleCategory(oldCat,newCat,this.catLst);

    }

    private Category getCategoryInSave(String catName) {

        for(int i = 0; i<catLst.size(); i++)
        {
            if(catLst.get(i).getCatName().equals(catName))
            {
                return catLst.get(i);
            }
        }
        //in case not father
        return findSingleCategory(catName,this.catLst);

    }

    private Category findSingleCategory(String catName, List<Category> catList)
    {
        Category c = null;
        List<Category> tempLst = catList;
        for (int i = 0; i < tempLst.size(); i++) {
            if(c!=null)
                return c;
            Category tempCat = tempLst.get(i).findSon(catName);
            if (tempCat != null) {
                return tempCat;
            }
            else
                c = findSingleCategory(catName,tempLst.get(i).get_sonCat());
        }
        return c;
    }

    public void InsertCategoryWithSons(String catName, List<String> catList) {
        Category father = getCategoryInSave(catName);
        Category newCat = null;
        if(!catList.isEmpty())
            newCat = new Category(catList.get(0));
        if(father !=null)
        {
            father.AddNewSonCategory(catList.get(0));
            newCat = father.findSon(catList.get(0));
            _cHandler.insert(newCat);
            for(int i = 1; i<catList.size(); i++)
            {
                newCat.AddNewSonCategory(catList.get(i));
                _cHandler.insert(newCat.findSon(catList.get(i)));
            }
        }

    }

    public void deleteCategory(String toDelete)
    {

        Category cat = null; //Category we want to delete
        if(getCategoryInSave(toDelete)!=null)
        {
            cat = getCategoryInSave(toDelete);
            if(cat.get_fatherCat()!=null)
            {
                Category fatherCat = cat.get_fatherCat();
                for(int i = 0; i<cat.get_sonCat().size();i++)
                    fatherCat.AddNewCategory(cat.get_sonCat().get(i));

                fatherCat.deleteSon(cat);
            }
            else
            {
                //deleting father category
                for(int i = 0; i<cat.get_sonCat().size();i++) {
                    //Test1
                    System.out.println();
                    cat.get_sonCat().get(i).set_fatherCat(null);
                    this.catLst.add(cat.get_sonCat().get(i));
                }

                List<Category> newList = new ArrayList<Category>() ;
                for (Category category : this.catLst)
                    if (!category.Compare(cat))
                        newList.add(category);

                this.catLst = newList;

            }
            _cHandler.delete(cat);

        }

        if(cat == null)
        {
            throw new NullPointerException("Category does not exists");
        }
    }

    public String print() {
       String res = "";
       res = res + this.catLst.get(0).getCatName();
       return res;
    }
}
