package com.example.demo.iface.dto;

import lombok.Data;

/**
 * Resource class for the Command API
 */
@Data
public class PageResource {

	/**
	 * 總頁數
	 */
	private int totalPages;

	/**
	 * 符合查詢條件的數據總數
	 */
	private long totalElements;

	/**
	 * 每頁的數據量（分頁大小）
	 */
	private int size;

	/**
	 * 當前頁碼（從 0 開始）
	 */
	private int number;

	/**
	 * 當前頁實際返回的數據數量
	 */
	private int numberOfElements;

	/**
	 * 是否為最後一頁
	 */
	private boolean last;

	/**
	 * 是否為第一頁
	 */
	private boolean first;

	/**
	 * 當前頁是否沒有數據
	 */
	private boolean empty;

}
