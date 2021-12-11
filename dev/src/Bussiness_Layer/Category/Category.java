package Bussiness_Layer.Category;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String _catName;
    private Category _fatherCat;

    private List<Category> _sonCat;
    public Category(String name)
    {
        _catName = name;
        _fatherCat = null;
        _sonCat = new ArrayList<Category>();
    }

    public Category get_fatherCat() {
        return _fatherCat;
    }

    public void set_fatherCat(Category _fatherCat) {
        this._fatherCat = _fatherCat;
    }

    public List<Category> get_sonCat()
    {
        return this._sonCat;
    }

    public boolean Compare(Category cat)
    {
        return this._catName.equals(cat.getCatName());
    }

    public String getCatName()
    {
        return this._catName;
    }

    public void UpdateCategoryName(String newName)
    {
        _catName = newName;
    }

    public void SetFatherCategory(Category cat)
    {
        _fatherCat = cat;
    }

    //FOR TESTS//
    public void AddNewSonCategory(Category cat)
    {
        cat.SetFatherCategory(this);
        _sonCat.add(cat);
    }

    public void AddNewSonCategory(String catName)
    {
        Category cat = new Category(catName);
        cat.SetFatherCategory(this);
        _sonCat.add(cat);
    }

    /**
     *
     * @param s - category name
     * @return null if no son
     */
    public Category findSon(String s)
    {
        Category temp = new Category(s);
        for(int i=0; i<_sonCat.size();i++)
        {
            if(temp.Compare(_sonCat.get(i)))
                return _sonCat.get(i);
        }

        return null;
    }
    public List<String> getfathersList()
    {
        List<String> catLst = new ArrayList<String>();
        catLst.add(this._catName);
        Category tmp = this;
        while(tmp._fatherCat != null){
            catLst.add(tmp._fatherCat._catName);
            tmp = tmp._fatherCat;
        }
        return catLst;
    }

    public String getFatherName()
    {
        if(_fatherCat == null)
            return null;
        return _fatherCat.getCatName();
    }

    public void AddNewCategory(Category cat)
    {
        this._sonCat.add(cat);
        cat.set_fatherCat(this);
    }

    public void deleteSon(Category c)
    {
        List<Category> newSons = new ArrayList<Category>();
        for(int i = 0; i<_sonCat.size(); i++)
        {
            if(!_sonCat.get(i).Compare(c))
                newSons.add(_sonCat.get(i));
        }
        _sonCat = newSons;

    }



}
