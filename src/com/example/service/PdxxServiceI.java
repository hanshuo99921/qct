package com.example.service;

import java.util.List;

import com.example.qct.util.Tpdxx;

public interface PdxxServiceI {
	public void save(Tpdxx pdxx);

	public void delete(Integer id);

	public void update(Tpdxx pdxx);

	public Tpdxx find(Integer id);

	public List<Tpdxx> find(int page, int pagesize);

	public long getCount();

	public long getToProceedCount();

	public List<Tpdxx> findToProceed();

	public void clear();
	
	public Tpdxx findByPdid(int pdid);

}
