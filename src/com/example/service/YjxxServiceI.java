package com.example.service;

import java.util.List;

import com.example.qct.util.Tyjxx;

public interface YjxxServiceI {
	public void save(Tyjxx yjxx);

	public void delete(Integer id);

	public void update(Tyjxx yjxx);

	public Tyjxx find(Integer id);

	public List<Tyjxx> find(int page, int pagesize);

	public long getCount();

	public int findPdidByTm(String tm);

}
