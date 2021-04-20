package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {


	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	// TESTBED - for testing purposes only
	/*@Bean
	public CommandLineRunner initParticipationData(PlayerRepository pRepository,
			GameRepository gmRepository, GamePlayerRepository gpRepository,
			ShipRepository shRepository, SalvoRepository slRepository,
			ScoreRepository scRepository) {
		return (args) -> {
			Player pla1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player pla2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player pla3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			Player pla4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole"));
			pRepository.save(pla1);
			pRepository.save(pla2);
			pRepository.save(pla3);
			pRepository.save(pla4);
			Date d1 = new Date();
			Date d2 = Date.from(d1.toInstant().plusSeconds(3600));
			Date d3 = Date.from(d2.toInstant().plusSeconds(3600));
			Date d4 = Date.from(d3.toInstant().plusSeconds(3600));
			Date d5 = Date.from(d4.toInstant().plusSeconds(3600));
			Date d6 = Date.from(d5.toInstant().plusSeconds(3600));
			Date d7 = Date.from(d6.toInstant().plusSeconds(3600));
			Date d8 = Date.from(d7.toInstant().plusSeconds(3600));
			Game gam1 = new Game(d1, 2);
			Game gam2 = new Game(d2, 2);
			Game gam3 = new Game(d3, 2);
			Game gam4 = new Game(d4, 2);
			Game gam5 = new Game(d5, 3);
			Game gam6 = new Game(d6, 0);
			Game gam7 = new Game(d7, 0);
			Game gam8 = new Game(d8, 0);
			gmRepository.save(gam1);
			gmRepository.save(gam2);
			gmRepository.save(gam3);
			gmRepository.save(gam4);
			gmRepository.save(gam5);
			gmRepository.save(gam6);
			gmRepository.save(gam7);
			gmRepository.save(gam8);
			GamePlayer par01 = new GamePlayer(d1);
			par01.setPlayer(pla1);
			par01.setGame(gam1);
			gpRepository.save(par01);
			GamePlayer par02 = new GamePlayer(d1);
			par02.setPlayer(pla2);
			par02.setGame(gam1);
			gpRepository.save(par02);
			GamePlayer par03 = new GamePlayer(d2, pla1, gam2);
			GamePlayer par04 = new GamePlayer(d2, pla2, gam2);
			GamePlayer par05 = new GamePlayer(d3, pla2, gam3);
			GamePlayer par06 = new GamePlayer(d3, pla4, gam3);
			GamePlayer par07 = new GamePlayer(d4, pla2, gam4);
			GamePlayer par08 = new GamePlayer(d4, pla1, gam4);
			GamePlayer par09 = new GamePlayer(d5, pla4, gam5);
			GamePlayer par10 = new GamePlayer(d5, pla1, gam5);
			GamePlayer par11 = new GamePlayer(d6, pla3, gam6);
			GamePlayer par12 = new GamePlayer(d7, pla4, gam7);
			GamePlayer par13 = new GamePlayer(d8, pla3, gam8);
			GamePlayer par14 = new GamePlayer(d8, pla4, gam8);
			gpRepository.save(par03);
			gpRepository.save(par04);
			gpRepository.save(par05);
			gpRepository.save(par06);
			gpRepository.save(par07);
			gpRepository.save(par08);
			gpRepository.save(par09);
			gpRepository.save(par10);
			gpRepository.save(par11);
			gpRepository.save(par12);
			gpRepository.save(par13);
			gpRepository.save(par14);
			List<String> loc01 = Arrays.asList("H2", "H3", "H4");
			List<String> loc02 = Arrays.asList("E1", "F1", "G1");
			List<String> loc03 = Arrays.asList("B4", "B5");
			List<String> loc04 = Arrays.asList("B5", "C5", "D5");
			List<String> loc05 = Arrays.asList("F1", "F2");
			List<String> loc06 = Arrays.asList("B5", "C5", "D5");
			List<String> loc07 = Arrays.asList("C6", "C7");
			List<String> loc08 = Arrays.asList("A2", "A3", "A4");
			List<String> loc09 = Arrays.asList("G6", "H6");
			List<String> loc10 = Arrays.asList("B5", "C5", "D5");
			List<String> loc11 = Arrays.asList("C6", "C7");
			List<String> loc12 = Arrays.asList("A2", "A3", "A4");
			List<String> loc13 = Arrays.asList("G6", "H6");
			List<String> loc14 = Arrays.asList("B5", "C5", "D5");
			List<String> loc15 = Arrays.asList("C6", "C7");
			List<String> loc16 = Arrays.asList("A2", "A3", "A4");
			List<String> loc17 = Arrays.asList("G6", "H6");
			List<String> loc18 = Arrays.asList("B5", "C5", "D5");
			List<String> loc19 = Arrays.asList("C6", "C7");
			List<String> loc20 = Arrays.asList("A2", "A3", "A4");
			List<String> loc21 = Arrays.asList("G6", "H6");
			List<String> loc22 = Arrays.asList("B5", "C5", "D5");
			List<String> loc23 = Arrays.asList("C6", "C7");
			List<String> loc24 = Arrays.asList("B5", "C5", "D5");
			List<String> loc25 = Arrays.asList("C6", "C7");
			List<String> loc26 = Arrays.asList("A2", "A3", "A4");
			List<String> loc27 = Arrays.asList("G6", "H6");
			shRepository.save(new Ship("Destroyer", par01, loc01));
			shRepository.save(new Ship("Submarine", par01, loc02));
			shRepository.save(new Ship("Patrol Boat", par01, loc03));
			shRepository.save(new Ship("Destroyer", par02, loc04));
			shRepository.save(new Ship("Patrol Boat", par02, loc05));
			shRepository.save(new Ship("Destroyer", par03, loc06));
			shRepository.save(new Ship("Patrol Boat", par03, loc07));
			shRepository.save(new Ship("Submarine", par04, loc08));
			shRepository.save(new Ship("Patrol Boat", par04, loc09));
			shRepository.save(new Ship("Destroyer", par05, loc10));
			shRepository.save(new Ship("Patrol Boat", par05, loc11));
			shRepository.save(new Ship("Submarine", par06, loc12));
			shRepository.save(new Ship("Patrol Boat", par06, loc13));
			shRepository.save(new Ship("Destroyer", par07, loc14));
			shRepository.save(new Ship("Patrol Boat", par07, loc15));
			shRepository.save(new Ship("Submarine", par08, loc16));
			shRepository.save(new Ship("Patrol Boat", par08, loc17));
			shRepository.save(new Ship("Destroyer", par09, loc18));
			shRepository.save(new Ship("Patrol Boat", par09, loc19));
			shRepository.save(new Ship("Submarine", par10, loc20));
			shRepository.save(new Ship("Patrol Boat", par10, loc21));
			shRepository.save(new Ship("Destroyer", par11, loc22));
			shRepository.save(new Ship("Patrol Boat", par11, loc23));
			shRepository.save(new Ship("Destroyer", par13, loc24));
			shRepository.save(new Ship("Patrol Boat", par13, loc25));
			shRepository.save(new Ship("Submarine", par14,loc26));
			shRepository.save(new Ship("Patrol Boat", par14, loc27));
			Set<String> slv01 = new HashSet<>(Arrays.asList("B5", "C5", "F1"));
			Set<String> slv02 = new HashSet<>(Arrays.asList("B4", "B5", "B6"));
			Set<String> slv03 = new HashSet<>(Arrays.asList("F2", "D5"));
			Set<String> slv04 = new HashSet<>(Arrays.asList("E1", "H3", "A2"));
			Set<String> slv05 = new HashSet<>(Arrays.asList("A2", "A4", "G6"));
			Set<String> slv06 = new HashSet<>(Arrays.asList("B5", "D5", "C7"));
			Set<String> slv07 = new HashSet<>(Arrays.asList("A3", "H6"));
			Set<String> slv08 = new HashSet<>(Arrays.asList("C5", "C6"));
			Set<String> slv09 = new HashSet<>(Arrays.asList("G6", "H6", "A4"));
			Set<String> slv10 = new HashSet<>(Arrays.asList("H1", "H2", "H3"));
			Set<String> slv11 = new HashSet<>(Arrays.asList("A2", "A3", "D8"));
			Set<String> slv12 = new HashSet<>(Arrays.asList("E1", "F2", "G3"));
			Set<String> slv13 = new HashSet<>(Arrays.asList("A3", "A4", "F7"));
			Set<String> slv14 = new HashSet<>(Arrays.asList("B5", "C6", "H1"));
			Set<String> slv15 = new HashSet<>(Arrays.asList("A2", "G6", "H6"));
			Set<String> slv16 = new HashSet<>(Arrays.asList("C5", "C7", "D5"));
			Set<String> slv17 = new HashSet<>(Arrays.asList("A1", "A2", "A3"));
			Set<String> slv18 = new HashSet<>(Arrays.asList("B5", "B6", "C7"));
			Set<String> slv19 = new HashSet<>(Arrays.asList("G6", "G7", "G8"));
			Set<String> slv20 = new HashSet<>(Arrays.asList("C6", "D6", "E6"));
			Set<String> slv21 = new HashSet<>(Arrays.asList("H1", "H8"));
			slRepository.save(new Salvo(par01, 1, slv01));
			slRepository.save(new Salvo(par02, 1, slv02));
			slRepository.save(new Salvo(par01, 2, slv03));
			slRepository.save(new Salvo(par02, 2, slv04));
			slRepository.save(new Salvo(par03, 1, slv05));
			slRepository.save(new Salvo(par04, 1, slv06));
			slRepository.save(new Salvo(par03, 2, slv07));
			slRepository.save(new Salvo(par04, 2, slv08));
			slRepository.save(new Salvo(par05, 1, slv09));
			slRepository.save(new Salvo(par06, 1, slv10));
			slRepository.save(new Salvo(par05, 2, slv11));
			slRepository.save(new Salvo(par06, 2, slv12));
			slRepository.save(new Salvo(par07, 1, slv13));
			slRepository.save(new Salvo(par08, 1, slv14));
			slRepository.save(new Salvo(par07, 2, slv15));
			slRepository.save(new Salvo(par08, 2, slv16));
			slRepository.save(new Salvo(par09, 1, slv17));
			slRepository.save(new Salvo(par10, 1, slv18));
			slRepository.save(new Salvo(par09, 2, slv19));
			slRepository.save(new Salvo(par10, 2, slv20));
			slRepository.save(new Salvo(par10, 3, slv21));
			Date d1end = Date.from(d1.toInstant().plusSeconds(1800));
			Date d2end = Date.from(d2.toInstant().plusSeconds(1800));
			Date d3end = Date.from(d3.toInstant().plusSeconds(1800));
			Date d4end = Date.from(d4.toInstant().plusSeconds(1800));
			scRepository.save(new Score(gam1, pla1, 1, d1end));
			scRepository.save(new Score(gam1, pla2, 0, d1end));
			scRepository.save(new Score(gam2, pla1, 0.5, d2end));
			scRepository.save(new Score(gam2, pla2, 0.5, d2end));
			scRepository.save(new Score(gam3, pla2, 1, d3end));
			scRepository.save(new Score(gam3, pla4, 0, d3end));
			scRepository.save(new Score(gam4, pla2, 0.5, d4end));
			scRepository.save(new Score(gam4, pla1, 0.5, d4end));
		};
	}*/
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(name -> {
			Player player = playerRepository.findByUserName(name);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + name);
			}
		});
	}
}


@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.authorizeRequests()
				.antMatchers("/api/games", "/home", "/api/login", "/api/players").permitAll()
				.anyRequest().fullyAuthenticated();

		http.formLogin()
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		// RM NOTE: commented one was the original way
		//http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
		http.formLogin().successHandler((req, res, auth) -> handleSuccessfulLogin(req, res));


		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

	}

	// RM NOTE: these two methods added in attempt to solve SameSite cookie issue
	private void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("--- enter handleSuccessfulLogin ---");
		System.out.println("--- first response Set-Cookie headers:");
		if (response.getHeaders("Set-Cookie").toArray().length >0 ) {
			System.out.println(response.getHeaders("Set-Cookie").toArray()[0]);
		} else {
			System.out.println(response.getHeaders("Set-Cookie").toArray().length);
		}

		clearAuthenticationAttributes(request);
		System.out.println("--- middle response Set-Cookie headers:");
		if (response.getHeaders("Set-Cookie").toArray().length >0 ) {
			System.out.println(response.getHeaders("Set-Cookie").toArray()[0]);
		} else {
			System.out.println(response.getHeaders("Set-Cookie").toArray().length);
		}

		addSameSiteCookieAttribute(response);
		System.out.println("--- final response Set-Cookie headers:");
		if (response.getHeaders("Set-Cookie").toArray().length >0 ) {
			System.out.println(response.getHeaders("Set-Cookie").toArray()[0]);
		} else {
			System.out.println(response.getHeaders("Set-Cookie").toArray().length);
		}

	}

	private void addSameSiteCookieAttribute(HttpServletResponse response) {
		System.out.println("--- enter addSameSiteCookieAttribute ---");
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		if (headers.size() == 0) {
			System.out.println("--- No Set-Cookie header present! Adding it");
			response.addHeader(HttpHeaders.SET_COOKIE, "SameSite=None; Secure");
		}
		boolean firstHeader = true;
		// there can be multiple Set-Cookie attributes
		for (String header : headers) {
			if (firstHeader) {
				System.out.println("--- first header");
				response.setHeader(HttpHeaders.SET_COOKIE,
						String.format("%s; %s", header, "SameSite=None; Secure"));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE,
					String.format("%s; %s", header, "SameSite=None; Secure"));
			System.out.println("---- added header. -----");
			System.out.println(headers.toString());
		}
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		System.out.println("--- enter clearAuthenticationAttributes ---");
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD",
				"GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// will fail with 403 Invalid CORS request
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}