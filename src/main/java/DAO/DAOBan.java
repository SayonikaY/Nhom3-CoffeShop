package DAO;

import Models.Ban;

import java.util.List;


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
}
