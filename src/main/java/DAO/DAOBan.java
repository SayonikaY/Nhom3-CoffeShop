package DAO;

import Models.Ban;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


public class DAOBan extends BaseDAO<Ban, Integer> {

    public DAOBan() {
        super(Ban.class);
    }
    /**
     * Find a Ban by its status
     * @return Optional containing the Ban if found, otherwise empty
     */
    public List<Ban> findBansByStatus(String trangthai) {
        return getCurrentSession().createQuery("FROM Ban WHERE trangThai = :trangthai", Ban.class)
                .setParameter("trangthai", trangthai)
                .list();
    }

    public void updateBanStatus(int id, String trangThai) {
        Ban ban = findById(id);
        if (ban != null) {
            ban.setTrangThai(trangThai);
            update(ban);
        }
    }
//    public List<Ban> findAll(){
//        List<Ban> list = null;
//        Session session = DB.openSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            Query <Ban> query =session.createQuery("FROM Ban", Ban.class);
//            list =query.list();
//            tx.commit();
//        } catch(Exception e) {
//            if (tx != null) tx.rollback();
//            e.printStackTrace();
//        } finally {
//            session.close();
//        }
//        return list;
//    }
    
}
