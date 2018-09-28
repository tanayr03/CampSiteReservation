package com.daoimpl;

import com.dao.BookingDAO;
import com.domain.Booking;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingDAOImpl implements BookingDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int save(Booking booking) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(booking);
		int bookingId = booking.getId();
		tx.commit();
		session.close();
		return bookingId;
	}

	@Override
	public Booking saveUpdate(Booking booking) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.merge(booking);
		int bookingId = booking.getId();
		tx.commit();
		session.close();
		return booking;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Booking> getList(int id) {
		Session session = this.sessionFactory.openSession();
		String hql = "FROM Booking B WHERE B.id =" + id;
		List<Booking> bookingList = session.createQuery(hql).list();
		System.out.println(hql);
		session.close();
		return bookingList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Booking> getListFromTill(LocalDate from, LocalDate till) {
		Session session = this.sessionFactory.openSession();
		String hql = "FROM Booking B WHERE B.arrival >='" +Date.valueOf(from) + "' AND B.departure <= '" + Date.valueOf(till) + "'";
		List<Booking> bookingList = session.createQuery(hql).list();
		System.out.println(hql);
		session.close();
		return bookingList;
	}

}
