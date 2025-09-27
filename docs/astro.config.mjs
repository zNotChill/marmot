// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
	site: "https://marmot.znotchill.me",
	base: "/docs/",
	integrations: [
		starlight({
			customCss: [
				"./src/styles/custom.css",
			],
			title: 'Marmot Developer API',
			social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/withastro/starlight' }],
			sidebar: [
				{
					label: 'Guides',
					items: [
						{ label: 'Get Started', slug: 'guides/get_started' },
						{
							label: 'Minestom',
							items: [
								{ label: 'Get Started', slug: 'guides/minestom/get_started' },
							]
						},
						{
							label: 'API',
							items: [
								{ label: 'Keybinds', slug: 'guides/api/keybinds' },
								{ label: 'UI', slug: 'guides/api/ui' },
								{ label: 'Camera Lock', slug: 'guides/api/camera_lock' },
								{ label: 'Camera', slug: 'guides/api/camera' },
								{ label: 'Camera Offset', slug: 'guides/api/camera_offset' },
								{ label: 'Marmot Player Data', slug: 'guides/api/marmot_player_data' },
							]
						},
					],
				},
				{
					label: 'Reference',
					autogenerate: { directory: 'reference' },
				},
			],
		}),
	],
});
