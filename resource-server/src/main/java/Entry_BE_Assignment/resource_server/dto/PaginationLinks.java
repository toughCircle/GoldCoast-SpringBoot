package Entry_BE_Assignment.resource_server.dto;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationLinks {
	private String previous;
	private String next;
	private int totalPages;
	private int currentPage;

	public static <T> PaginationLinks from(Page<T> page) {
		String previousLink = page.hasPrevious() ? "previousPageLink" : null;
		String nextLink = page.hasNext() ? "nextPageLink" : null;
		return new PaginationLinks(previousLink, nextLink, page.getTotalPages(), page.getNumber() + 1);
	}
}
